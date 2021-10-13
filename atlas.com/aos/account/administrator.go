package account

import (
	"gorm.io/gorm"
	"time"
)

func createAccount(db *gorm.DB, name string, password string) (*Model, error) {
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

func updateState(db *gorm.DB, id uint32, state byte) error {
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

func makeAccount(a *account) *Model {
	r := Builder(a.ID).
		SetName(a.Name).
		SetPassword(a.Password).
		SetState(a.State).
		Build()
	return &r
}
