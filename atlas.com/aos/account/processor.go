package account

import (
	"atlas-aos/database"
	"atlas-aos/domain"
	"errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

func SetLoggedIn(db *gorm.DB) domain.IdOperator {
	return func(id uint32) error {
		return ForId(db)(id, setLoggedIn(db))
	}
}

func SetLoggedOut(db *gorm.DB) domain.IdOperator {
	return func(id uint32) error {
		return ForId(db)(id, setLoggedOut(db))
	}
}

func setLoggedIn(db *gorm.DB) domain.ModelOperator[Model] {
	return func(m Model) error {
		return update(db)(updateState(StateLoggedIn))(m.Id())
	}
}

func setLoggedOut(db *gorm.DB) domain.ModelOperator[Model] {
	return func(m Model) error {
		return update(db)(updateState(StateNotLoggedIn))(m.Id())
	}
}

func ForId(db *gorm.DB) func(id uint32, operator domain.ModelOperator[Model]) error {
	return func(id uint32, operator domain.ModelOperator[Model]) error {
		return domain.For[Model](byIdProvider(db)(id), operator)
	}
}

func byIdProvider(db *gorm.DB) func(id uint32) domain.ModelProvider[Model] {
	return func(id uint32) domain.ModelProvider[Model] {
		return database.ModelProvider[Model, entity](db)(entityById(id), modelFromEntity)
	}
}

func byNameProvider(db *gorm.DB) func(name string) domain.ModelListProvider[Model] {
	return func(name string) domain.ModelListProvider[Model] {
		return database.ModelListProvider[Model, entity](db)(entitiesByName(name), modelFromEntity)
	}
}

func GetById(l logrus.FieldLogger, db *gorm.DB) func(id uint32) (*Model, error) {
	return func(id uint32) (*Model, error) {
		m, err := byIdProvider(db)(id)()
		if err != nil {
			l.WithError(err).Errorf("Unable to retrieve account by id %d.", id)
			return nil, err
		}
		return &m, nil
	}
}

func GetByName(l logrus.FieldLogger, db *gorm.DB) func(name string) (*Model, error) {
	return func(name string) (*Model, error) {
		ms, err := byNameProvider(db)(name)()
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

func GetOrCreate(l logrus.FieldLogger, db *gorm.DB) func(name string, password string, automaticRegister bool) (*Model, error) {
	return func(name string, password string, automaticRegister bool) (*Model, error) {
		ms, _ := byNameProvider(db)(name)()
		if ms != nil && len(ms) != 0 {
			return &ms[0], nil
		}

		if !automaticRegister {
			l.Errorf("Unable to locate account by name %s, and automatic account creation is not enabled.", name)
			return nil, errors.New("account not found")
		}

		l.Debugf("Attempting to create account %s, with password %s.", name, password)
		hashPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
		if err != nil {
			l.WithError(err).Errorf("Error generating hash when creating account %s.", name)
			return nil, err
		}

		m, err := create(db)(name, string(hashPass))
		if err != nil {
			l.WithError(err).Errorf("Unable to create account %s.", name)
			return nil, err
		}
		return &m, nil
	}
}
