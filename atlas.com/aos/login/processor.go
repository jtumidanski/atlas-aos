package login

import (
	"atlas-aos/account"
	"atlas-aos/configuration"
	"errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

const (
	SystemError       = "SYSTEM_ERROR"
	NotRegistered     = "NOT_REGISTERED"
	DeletedOrBlocked  = "DELETED_OR_BLOCKED"
	AlreadyLoggedIn   = "ALREADY_LOGGED_IN"
	IncorrectPassword = "INCORRECT_PASSWORD"
)

func AttemptLogin(l logrus.FieldLogger, db *gorm.DB) func(sessionId uint32, name string, password string) error {
	return func(sessionId uint32, name string, password string) error {
		if checkLoginAttempts(sessionId) > 4 {
			return errors.New("TOO_MANY_ATTEMPTS")
		}

		c, err := configuration.Get()
		if err != nil {
			l.WithError(err).Errorf("Error reading needed configuration.")
			return errors.New(SystemError)
		}

		a, err := account.GetOrCreate(l, db)(name, password, c.AutomaticRegister)
		if err != nil && !c.AutomaticRegister {
			return errors.New(NotRegistered)
		}
		if err != nil {
			return errors.New(SystemError)
		}

		if a.Banned() {
			return errors.New(DeletedOrBlocked)
		}

		// TODO implement ip, mac, and temporary banning practices

		if a.State() != account.StateNotLoggedIn {
			return errors.New(AlreadyLoggedIn)
		} else if a.Password()[0] == uint8('$') && a.Password()[1] == uint8('2') && bcrypt.CompareHashAndPassword([]byte(a.Password()), []byte(password)) == nil {
			// TODO implement tos tracking
		} else {
			return errors.New(IncorrectPassword)
		}

		err = account.SetLoggedIn(db)(a.Id())
		if err != nil {
			l.WithError(err).Errorf("Error trying to update logged in state for %s.", name)
			return errors.New(SystemError)
		}

		return nil
	}
}

func checkLoginAttempts(sessionId uint32) byte {
	return 0
}
