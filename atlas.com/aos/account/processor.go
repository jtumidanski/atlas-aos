package account

import (
	"atlas-aos/database"
	"atlas-aos/domain"
	"errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

func SetLoggedIn(_ logrus.FieldLogger, db *gorm.DB) func(accountId uint32) error {
	return func(accountId uint32) error {
		return update(db, accountId, UpdateState(StateLoggedIn))
	}
}

func SetLoggedOut(_ logrus.FieldLogger, db *gorm.DB) func(accountId uint32) error {
	return func(accountId uint32) error {
		return update(db, accountId, UpdateState(StateNotLoggedIn))
	}
}

func GetById(l logrus.FieldLogger, db *gorm.DB) func(id uint32) (*Model, error) {
	return func(id uint32) (*Model, error) {
		m, err := database.ModelProvider[Model, entity](db)(entityById(id), makeAccount)()
		if err != nil {
			l.WithError(err).Errorf("Unable to retrieve account by id %d.", id)
			return nil, err
		}
		return &m, nil
	}
}

func GetByName(l logrus.FieldLogger, db *gorm.DB) func(name string) (*Model, error) {
	return func(name string) (*Model, error) {
		ms, err := database.ModelListProvider[Model, entity](db)(entitiesByName(name), makeAccount)()
		if err != nil {
			l.WithError(err).Errorf("Unable to locate account with name %s.", name)
			return nil, err
		}
		m, err := domain.First[Model](ms)
		if err != nil {
			l.WithError(err).Errorf("Unable to locate account with name %s.", name)
			return nil, err
		}
		return &m, nil
	}
}

func GetOrCreate(l logrus.FieldLogger, db *gorm.DB) func(name string, password string, create bool) (*Model, error) {
	return func(name string, password string, create bool) (*Model, error) {
		ms, _ := database.ModelListProvider[Model, entity](db)(entitiesByName(name), makeAccount)()
		if ms != nil && len(ms) != 0 {
			return &ms[0], nil
		}

		if !create {
			l.Errorf("Unable to locate account by name %s, and automatic account creation is not enabled.", name)
			return nil, errors.New("account not found")
		}

		l.Debugf("Attempting to create account %s, with password %s.", name, password)
		hashPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
		if err != nil {
			l.WithError(err).Errorf("Error generating hash when creating account %s.", name)
			return nil, err
		}

		m, err := createAccount(db, name, string(hashPass))
		if err != nil {
			l.WithError(err).Errorf("Unable to create account %s.", name)
			return nil, err
		}
		return &m, nil
	}
}
