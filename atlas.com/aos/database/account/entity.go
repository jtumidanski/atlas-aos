package account

import (
   "gorm.io/gorm"
)

func Migration(db *gorm.DB) {
   _ = db.AutoMigrate(&account{})
}

type account struct {
   ID        uint32 `gorm:"primaryKey;autoIncrement;not null"`
   Name      string `gorm:"not null"`
   Password  string `gorm:"not null"`
   State     byte   `gorm:"not null;default=0"`
   LastLogin int64
}
