package consumers

import (
	"atlas-aos/processors"
	"gorm.io/gorm"
	"log"
)

type characterStatusEvent struct {
	WorldId     byte   `json:"worldId"`
	ChannelId   byte   `json:"channelId"`
	AccountId   uint32 `json:"accountId"`
	CharacterId uint32 `json:"characterId"`
	Type        string `json:"type"`
}

func CharacterStatusEventCreator() EmptyEventCreator {
	return func() interface{} {
		return &characterStatusEvent{}
	}
}

func HandleCharacterStatusEvent(db *gorm.DB) EventProcessor {
	return func(l *log.Logger, e interface{}) {
		if event, ok := e.(*characterStatusEvent); ok {
			if event.Type == "LOGIN" {
				err := processors.SetLoggedIn(db, event.AccountId)
				if err != nil {
					l.Println("[ERROR] setting logged in state for account %d.", event.AccountId)
				}
			} else if event.Type == "LOGOUT" {
				err := processors.SetLoggedOut(db, event.AccountId)
				if err != nil {
					l.Println("[ERROR] setting logged out state for account %d.", event.AccountId)
				}
			} else {
				l.Println("[WARN] received a unhandled character status type of %s.", event.Type)
			}
		} else {
			l.Printf("[ERROR] unable to cast event provided to handler [HandleCharacterStatusEvent]")
		}
	}
}
