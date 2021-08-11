package consumers

import (
	"atlas-aos/kafka/handler"
	"context"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"sync"
)

func CreateEventConsumers(l *logrus.Logger, db *gorm.DB, ctx context.Context, wg *sync.WaitGroup) {
	cec := func(topicToken string, emptyEventCreator handler.EmptyEventCreator, processor handler.EventHandler) {
		createEventConsumer(l, ctx, wg, topicToken, emptyEventCreator, processor)
	}
	cec("TOPIC_CHARACTER_STATUS", CharacterStatusEventCreator(), HandleCharacterStatusEvent(db))

}

func createEventConsumer(l *logrus.Logger, ctx context.Context, wg *sync.WaitGroup, topicToken string, emptyEventCreator handler.EmptyEventCreator, processor handler.EventHandler) {
	wg.Add(1)
	go NewConsumer(l, ctx, wg, topicToken, "Account Orchestration Service", emptyEventCreator, processor)
}