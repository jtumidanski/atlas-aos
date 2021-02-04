package main

import (
	"atlas-aos/database/account"
	"atlas-aos/kafka/consumers"
	"atlas-aos/rest"
	"atlas-aos/retry"
	"context"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"
	"os"
	"os/signal"
	"syscall"
)

func connectToDatabase(attempt int) (bool, interface{}, error) {
	db, err := gorm.Open(mysql.New(mysql.Config{
		DSN:                       "root:the@tcp(atlas-db:3306)/atlas-aos?charset=utf8&parseTime=True&loc=Local",
		DefaultStringSize:         256,
		DisableDatetimePrecision:  true,
		DontSupportRenameIndex:    true,
		DontSupportRenameColumn:   true,
		SkipInitializeWithVersion: false,
	}), &gorm.Config{})
	if err != nil {
		return true, nil, err
	}
	return false, db, err
}

func main() {
	l := log.New(os.Stdout, "aos ", log.LstdFlags|log.Lmicroseconds)
	r, err := retry.RetryResponse(connectToDatabase, 10)
	if err != nil {
		panic("failed to connect database")
	}
	db := r.(*gorm.DB)

	// Migrate the schema
	account.Migration(db)

	createEventConsumers(l, db)
	createRestService(l, db)

	// trap sigterm or interrupt and gracefully shutdown the server
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt, os.Kill, syscall.SIGTERM)

	// Block until a signal is received.
	sig := <-c
	l.Println("[INFO] shutting down via signal:", sig)
}

func createEventConsumers(l *log.Logger, db *gorm.DB) {
	cec := func(topicToken string, emptyEventCreator consumers.EmptyEventCreator, processor consumers.EventProcessor) {
		createEventConsumer(l, topicToken, emptyEventCreator, processor)
	}
	cec("TOPIC_CHARACTER_STATUS", consumers.CharacterStatusEventCreator(), consumers.HandleCharacterStatusEvent(db))
}

func createEventConsumer(l *log.Logger, topicToken string, emptyEventCreator consumers.EmptyEventCreator, processor consumers.EventProcessor) {
	h := func(logger *log.Logger, event interface{}) {
		processor(logger, event)
	}

	c := consumers.NewConsumer(l, context.Background(), h,
		consumers.SetGroupId("Account Orchestration Service"),
		consumers.SetTopicToken(topicToken),
		consumers.SetEmptyEventCreator(emptyEventCreator))
	go c.Init()
}

func createRestService(l *log.Logger, db *gorm.DB) {
	rs := rest.NewServer(l, db)
	go rs.Run()
}
