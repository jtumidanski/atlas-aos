package consumers

import (
	"atlas-aos/kafka/handler"
	"atlas-aos/login"
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

func CharacterStatusEventCreator() handler.EmptyEventCreator {
	return func() interface{} {
		return &characterStatusEvent{}
	}
}

func HandleCharacterStatusEvent(db *gorm.DB) handler.EventHandler {
	return func(l logrus.FieldLogger, span opentracing.Span, e interface{}) {
		if event, ok := e.(*characterStatusEvent); ok {
			if event.Type == "LOGIN" {
				err := login.SetLoggedIn(db, event.AccountId)
				if err != nil {
					l.WithError(err).Errorf("Setting logged in state for account %d.", event.AccountId)
				}
			} else if event.Type == "LOGOUT" {
				err := login.SetLoggedOut(db, event.AccountId)
				if err != nil {
					l.WithError(err).Errorf("Setting logged out state for account %d.", event.AccountId)
				}
			} else {
				l.Warnf("Received a unhandled character status type of %s.", event.Type)
			}
		} else {
			l.Errorf("Unable to cast event provided.")
		}
	}
}
