package account

import (
	"gorm.io/gorm"
)

func GetByName(db *gorm.DB, name string) []*Model {
   var results []account
   err := db.Where(&account{Name: name}).First(&results).Error
   if err != nil {
      return make([]*Model, 0)
   }

   var accounts []*Model
   for _, a := range results {
      accounts = append(accounts, makeAccount(&a))
   }
   return accounts
}

func GetById(db *gorm.DB, id uint32) (*Model, error) {
   var result account
   err := db.First(&result, id).Error
   if err != nil {
      return nil, err
   }
   return makeAccount(&result), nil
}
