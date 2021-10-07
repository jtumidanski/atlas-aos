package consumers

import (
	"atlas-aos/kafka/handler"
	"context"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"sync"
)

const (
	CharacterStatus = "character_status"
)

func CreateEventConsumers(l *logrus.Logger, db *gorm.DB, ctx context.Context, wg *sync.WaitGroup) {
	cec := func(topicToken string, name string, emptyEventCreator handler.EmptyEventCreator, processor handler.EventHandler) {
		createEventConsumer(l, ctx, wg, name, topicToken, emptyEventCreator, processor)
	}
	cec("TOPIC_CHARACTER_STATUS", CharacterStatus, CharacterStatusEventCreator(), HandleCharacterStatusEvent(db))

}

func createEventConsumer(l *logrus.Logger, ctx context.Context, wg *sync.WaitGroup, name string, topicToken string, emptyEventCreator handler.EmptyEventCreator, processor handler.EventHandler) {
	wg.Add(1)
	go NewConsumer(l, ctx, wg, name, topicToken, "Account Orchestration Service", emptyEventCreator, processor)
}
