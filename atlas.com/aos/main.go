package main

import (
   "atlas-aos/database/account"
   "gorm.io/driver/mysql"
   "gorm.io/gorm"
   "log"
   "os"
)

func main() {
   l := log.New(os.Stdout, "aos ", log.LstdFlags|log.Lmicroseconds)
   db, err := gorm.Open(mysql.New(mysql.Config{
      DSN:                       "root:the@tcp(atlas-db:3306)/atlas-aos?charset=utf8&parseTime=True&loc=Local", // data source name
      DefaultStringSize:         256,                                                                            // default size for string fields
      DisableDatetimePrecision:  true,                                                                           // disable datetime precision, which not supported before MySQL 5.6
      DontSupportRenameIndex:    true,                                                                           // drop & create when rename index, rename index not supported before MySQL 5.7, MariaDB
      DontSupportRenameColumn:   true,                                                                           // `change` when rename column, rename column not supported before MySQL 8, MariaDB
      SkipInitializeWithVersion: false,                                                                          // auto configure based on currently MySQL version
   }), &gorm.Config{})
   if err != nil {
      panic("failed to connect database")
   }

   // Migrate the schema
   account.Migration(db)

   a := account.CreateAccount(db, "Justin", "Durr")
   l.Printf("Created account %d for %s with password %s.", a.Id(), a.Name(), a.Password())
}
