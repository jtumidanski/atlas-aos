package account

import (
	"gorm.io/gorm"
)

func getByName(db *gorm.DB, name string) modelListProvider {
	return func() ([]*Model, error) {
		var results []account
		err := db.Where(&account{Name: name}).First(&results).Error
		if err != nil {
			return make([]*Model, 0), nil
		}

		var accounts []*Model
		for _, a := range results {
			accounts = append(accounts, makeAccount(&a))
		}
		return accounts, nil
	}
}

func getById(db *gorm.DB, id uint32) modelProvider {
	return func() (*Model, error) {
		var result account
		err := db.First(&result, id).Error
		if err != nil {
			return nil, err
		}
		return makeAccount(&result), nil
	}
}
