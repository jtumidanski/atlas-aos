package account

import (
	"atlas-aos/domain"
	"gorm.io/gorm"
	"time"
)

func CreateAccount(db *gorm.DB, name string, password string) (*domain.Account, error) {
	a := &account{
		Name:      name,
		Password:  password,
		State:     0,
		LastLogin: time.Now().UnixNano(),
	}

	err := db.Create(a).Error
	if err != nil {
		return nil, err
	}

	return makeAccount(a), nil
}

func UpdateState(db *gorm.DB, id uint32, state byte) error {
	a := account{ID: id}
	err := db.Where(&a).First(&a).Error
	if err != nil {
		return err
	}

	a.State = state
	if state == StateLoggedIn {
		a.LastLogin = time.Now().UnixNano()
	}
	err = db.Save(&a).Error
	return err
}

func makeAccount(a *account) *domain.Account {
	r := domain.NewAccountBuilder(a.ID).
		SetName(a.Name).
		SetPassword(a.Password).
		SetState(a.State).
		Build()
	return &r
}
