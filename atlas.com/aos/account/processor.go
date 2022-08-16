package account

import (
	"atlas-aos/database"
	"atlas-aos/model"
	"errors"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type IdOperator func(uint32) error

func SetLoggedIn(db *gorm.DB) IdOperator {
	return func(id uint32) error {
		return ForId(db)(id, setLoggedIn(db))
	}
}

func SetLoggedOut(db *gorm.DB) IdOperator {
	return func(id uint32) error {
		return ForId(db)(id, setLoggedOut(db))
	}
}

func setLoggedIn(db *gorm.DB) model.Operator[Model] {
	return func(m Model) error {
		return update(db)(updateState(StateLoggedIn))(m.Id())
	}
}

func setLoggedOut(db *gorm.DB) model.Operator[Model] {
	return func(m Model) error {
		return update(db)(updateState(StateNotLoggedIn))(m.Id())
	}
}

func ForId(db *gorm.DB) func(id uint32, operator model.Operator[Model]) error {
	return func(id uint32, operator model.Operator[Model]) error {
		m, err := byIdProvider(db)(id)()
		if err != nil {
			return err
		}
		return operator(m)
	}
}

func byIdProvider(db *gorm.DB) func(id uint32) model.Provider[Model] {
	return func(id uint32) model.Provider[Model] {
		return database.ModelProvider[Model, entity](db)(entityById(id), modelFromEntity)
	}
}

func byNameProvider(db *gorm.DB) func(name string) model.SliceProvider[Model] {
	return func(name string) model.SliceProvider[Model] {
		return database.ModelListProvider[Model, entity](db)(entitiesByName(name), modelFromEntity)
	}
}

func GetById(l logrus.FieldLogger, db *gorm.DB) func(id uint32) (Model, error) {
	return func(id uint32) (Model, error) {
		m, err := byIdProvider(db)(id)()
		if err != nil {
			l.WithError(err).Errorf("Unable to retrieve account by id %d.", id)
			return Model{}, err
		}
		return m, nil
	}
}

func GetByName(l logrus.FieldLogger, db *gorm.DB) func(name string) (Model, error) {
	return func(name string) (Model, error) {
		m, err := model.First[Model](byNameProvider(db)(name))
		if err != nil {
			l.WithError(err).Errorf("Unable to locate account with name %s.", name)
			return Model{}, err
		}
		return m, nil
	}
}

func GetOrCreate(l logrus.FieldLogger, db *gorm.DB, span opentracing.Span) func(name string, password string, automaticRegister bool) (Model, error) {
	return func(name string, password string, automaticRegister bool) (Model, error) {
		m, err := model.First[Model](byNameProvider(db)(name))
		if err == nil {
			return m, nil
		}

		if !automaticRegister {
			l.Errorf("Unable to locate account by name %s, and automatic account creation is not enabled.", name)
			return Model{}, errors.New("account not found")
		}

		return Create(l, db, span)(name, password)
	}
}

func Create(l logrus.FieldLogger, db *gorm.DB, span opentracing.Span) func(name string, password string) (Model, error) {
	return func(name string, password string) (Model, error) {
		l.Debugf("Attempting to create account %s, with password %s.", name, password)
		hashPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
		if err != nil {
			l.WithError(err).Errorf("Error generating hash when creating account %s.", name)
			return Model{}, err
		}

		m, err := create(db)(name, string(hashPass))
		if err != nil {
			l.WithError(err).Errorf("Unable to create account %s.", name)
			return Model{}, err
		}
		emitCreatedEvent(l, span)(m.Id(), name)
		return m, nil
	}
}
