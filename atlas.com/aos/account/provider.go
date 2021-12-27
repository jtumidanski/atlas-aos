package account

import (
   "atlas-aos/database"
   "gorm.io/gorm"
)

func entityById(id uint32) database.EntityProvider[account] {
   return func(db *gorm.DB) (account, error) {
      var result account
      err := db.First(&result, id).Error
      return result, err
   }
}

func entitiesByName(name string) database.EntityListProvider[account] {
   return func(db *gorm.DB) ([]account, error) {
      var results []account
      err := db.Where(&account{Name: name}).First(&results).Error
      return results, err
   }
}
