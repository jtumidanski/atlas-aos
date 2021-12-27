package consumers

import (
	"atlas-aos/account"
	"atlas-aos/kafka/handler"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
)

type characterStatusEvent struct {
	WorldId     byte   `json:"worldId"`
	ChannelId   byte   `json:"channelId"`
	AccountId   uint32 `json:"accountId"`
	CharacterId uint32 `json:"characterId"`
	Type        string `json:"type"`
}

func HandleCharacterStatusEvent(db *gorm.DB) handler.EventHandler[characterStatusEvent] {
	return func(l logrus.FieldLogger, span opentracing.Span, event characterStatusEvent) {
		if event.Type == "LOGIN" {
			err := account.SetLoggedIn(l, db)(event.AccountId)
			if err != nil {
				l.WithError(err).Errorf("Setting logged in state for account %d.", event.AccountId)
			}
		} else if event.Type == "LOGOUT" {
			err := account.SetLoggedOut(l, db)(event.AccountId)
			if err != nil {
				l.WithError(err).Errorf("Setting logged out state for account %d.", event.AccountId)
			}
		} else {
			l.Warnf("Received a unhandled character status type of %s.", event.Type)
		}
	}
}
