package status

import (
	"atlas-aos/account"
	"atlas-aos/kafka/consumers"
	"atlas-aos/kafka/handler"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
)

const (
	consumerName = "character_status"
	topicToken   = "TOPIC_CHARACTER_STATUS"
	typeLogin    = "LOGIN"
	typeLogout   = "LOGOUT"
)

type event struct {
	WorldId     byte   `json:"worldId"`
	ChannelId   byte   `json:"channelId"`
	AccountId   uint32 `json:"accountId"`
	CharacterId uint32 `json:"characterId"`
	Type        string `json:"type"`
}

func NewConsumer(db *gorm.DB) func(groupId string) consumers.Config[event] {
	return func(groupId string) consumers.Config[event] {
		return consumers.NewConfiguration[event](consumerName, topicToken, groupId, HandleEvent(db))
	}
}

func HandleEvent(db *gorm.DB) handler.EventHandler[event] {
	return func(l logrus.FieldLogger, span opentracing.Span, event event) {
		switch event.Type {
		case typeLogin:
			handleLogin(l, db)(event.AccountId)
		case typeLogout:
			handleLogout(l, db)(event.AccountId)
		default:
			l.Warnf("Received a unhandled character status type of %s.", event.Type)
		}
	}
}

func handleLogout(l logrus.FieldLogger, db *gorm.DB) func(accountId uint32) {
	return func(accountId uint32) {
		err := account.SetLoggedOut(db)(accountId)
		if err != nil {
			l.WithError(err).Errorf("Setting logged out state for account %d.", accountId)
		}
	}
}

func handleLogin(l logrus.FieldLogger, db *gorm.DB) func(accountId uint32) {
	return func(accountId uint32) {
		err := account.SetLoggedIn(db)(accountId)
		if err != nil {
			l.WithError(err).Errorf("Setting logged in state for account %d.", accountId)
		}
	}
}
