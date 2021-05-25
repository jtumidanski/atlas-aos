package account

import (
   "atlas-aos/domain"
   "gorm.io/gorm"
)

func GetByName(db *gorm.DB, name string) []*domain.Account {
   var results []account
   err := db.Where(&account{Name: name}).First(&results).Error
   if err != nil {
      return make([]*domain.Account, 0)
   }

   var accounts []*domain.Account
   for _, a := range results {
      accounts = append(accounts, makeAccount(&a))
   }
   return accounts
}

func GetById(db *gorm.DB, id uint32) (*domain.Account, error) {
   var result account
   err := db.First(&result, id).Error
   if err != nil {
      return nil, err
   }
   return makeAccount(&result), nil
}
