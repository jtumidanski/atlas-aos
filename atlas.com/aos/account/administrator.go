package account

import (
	"gorm.io/gorm"
	"time"
)

type EntityUpdateFunction func() ([]string, func(e *entity))

func createAccount(db *gorm.DB, name string, password string) (Model, error) {
	a := &entity{
		Name:      name,
		Password:  password,
		State:     0,
		LastLogin: time.Now().UnixNano(),
	}

	err := db.Create(a).Error
	if err != nil {
		return Model{}, err
	}

	return makeAccount(*a), nil
}

func update(db *gorm.DB, id uint32, modifiers ...EntityUpdateFunction) error {
	e := &entity{}

	var columns []string
	for _, modifier := range modifiers {
		c, u := modifier()
		columns = append(columns, c...)
		u(e)
	}
	return db.Model(&entity{ID: id}).Select(columns).Updates(e).Error
}

func UpdateState(state byte) EntityUpdateFunction {
	return func() ([]string, func(e *entity)) {
		return []string{"State", "LastLogin"}, func(e *entity) {
			e.State = state
			if state == StateLoggedIn {
				e.LastLogin = time.Now().UnixNano()
			}
		}
	}
}

func makeAccount(a entity) Model {
	r := Builder(a.ID).
		SetName(a.Name).
		SetPassword(a.Password).
		SetState(a.State).
		Build()
	return r
}
