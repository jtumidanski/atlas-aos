package account

import (
   "atlas-aos/domain"
   "gorm.io/gorm"
)

func GetByName(db *gorm.DB, name string) []domain.Account {
   var results []account
   db.Where(&account{Name: name}).First(&results)

   var accounts []domain.Account
   for _, a := range results {
      accounts = append(accounts, makeAccount(&a))
   }
   return accounts
}

func GetById(db *gorm.DB, id uint32) domain.Account {
   var result account
   db.First(&result, id)

   return makeAccount(&result)
}
