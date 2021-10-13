package account

import (
	"errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type modelProvider func() (*Model, error)

type modelListProvider func() ([]*Model, error)

func SetLoggedIn(_ logrus.FieldLogger, db *gorm.DB) func(accountId uint32) error {
	return func(accountId uint32) error {
		return updateState(db, accountId, StateLoggedIn)
	}
}

func SetLoggedOut(_ logrus.FieldLogger, db *gorm.DB) func(accountId uint32) error {
	return func(accountId uint32) error {
		return updateState(db, accountId, StateNotLoggedIn)
	}
}

func GetById(l logrus.FieldLogger, db *gorm.DB) func(id uint32) (*Model, error) {
	return func(id uint32) (*Model, error) {
		m, err := getById(db, id)()
		if err != nil {
			l.WithError(err).Errorf("Unable to retrieve account by id %d.", id)
			return nil, err
		}
		return m, nil
	}
}

func GetByName(l logrus.FieldLogger, db *gorm.DB) func(name string) (*Model, error) {
	return func(name string) (*Model, error) {
		ms, err := getByName(db, name)()
		if err != nil {
			l.WithError(err).Errorf("Unable to locate account with name %s.", name)
			return nil, err
		}
		return ms[0], nil
	}
}

func GetOrCreate(l logrus.FieldLogger, db *gorm.DB) func(name string, password string, create bool) (*Model, error) {
	return func(name string, password string, create bool) (*Model, error) {
		ms, _ := getByName(db, name)()
		if ms == nil || len(ms) == 0 {
			if !create {
				l.Errorf("Unable to locate account by name %s, and automatic account creation is not enabled.", name)
				return nil, errors.New("account not found")
			}

			hashPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
			if err != nil {
				l.WithError(err).Errorf("Error generating hash when creating account %s.", name)
				l.WithError(err).Debugf("Name %s, password %s.", name, password)
				return nil, err
			}

			return createAccount(db, name, string(hashPass))
		}
		return ms[0], nil
	}
}
