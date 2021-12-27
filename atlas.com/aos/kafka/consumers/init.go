package consumers

import (
	"context"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"sync"
)

const (
	GroupId         = "Account Orchestration Service"
	CharacterStatus = "character_status"
)

func CreateEventConsumers(l *logrus.Logger, db *gorm.DB, ctx context.Context, wg *sync.WaitGroup) {
	go NewConsumer(l, ctx, wg, CharacterStatus, "TOPIC_CHARACTER_STATUS", GroupId, HandleCharacterStatusEvent(db))
}
