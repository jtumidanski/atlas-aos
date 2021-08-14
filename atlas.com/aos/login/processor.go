package login

import (
	account2 "atlas-aos/account"
	"atlas-aos/configuration"
	"errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

func SetLoggedIn(db *gorm.DB, accountId uint32) error {
	return account2.UpdateState(db, accountId, account2.StateLoggedIn)
}

func SetLoggedOut(db *gorm.DB, accountId uint32) error {
	return account2.UpdateState(db, accountId, account2.StateNotLoggedIn)
}

func AttemptLogin(l logrus.FieldLogger, db *gorm.DB, sessionId uint32, name string, password string) error {
	if checkLoginAttempts(sessionId) > 4 {
		return errors.New("TOO_MANY_ATTEMPTS")
	}

	var a *account2.Model
	as := account2.GetByName(db, name)
	if as == nil || len(as) == 0 {
		c, err := configuration.Get()
		if err != nil {
			l.WithError(err).Errorf("Error reading needed configuration.")
			return errors.New("SYSTEM_ERROR")
		}

		autoConfigure := c.AutomaticRegister
		if autoConfigure {
			hashPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
			if err != nil {
				return err
			}
			a, err = account2.CreateAccount(db, name, string(hashPass))
			if err != nil {
				l.WithError(err).Errorf("Error creating new account for %s.", name)
				return errors.New("SYSTEM_ERROR")
			}
		} else {
			return errors.New("NOT_REGISTERED")
		}
	} else {
		a = as[0]
	}

	if a.Banned() {
		return errors.New("DELETED_OR_BLOCKED")
	}

	// TODO implement ip, mac, and temporary banning practices

	if a.State() != account2.StateNotLoggedIn {
		return errors.New("ALREADY_LOGGED_IN")
	} else if a.Password()[0] == uint8('$') && a.Password()[1] == uint8('2') && bcrypt.CompareHashAndPassword([]byte(a.Password()), []byte(password)) == nil {
		// TODO implement tos tracking
	} else {
		return errors.New("INCORRECT_PASSWORD")
	}

	err := account2.UpdateState(db, a.Id(), account2.StateLoggedIn)
	if err != nil {
		l.WithError(err).Errorf("Error trying to update logged in state for %s.", name)
		return errors.New("SYSTEM_ERROR")
	}

	return nil
}

func checkLoginAttempts(sessionId uint32) byte {
	return 0
}
