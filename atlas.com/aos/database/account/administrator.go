package account

import (
   "atlas-aos/domain"
   "gorm.io/gorm"
   "time"
)

func CreateAccount(db *gorm.DB, name string, password string) domain.Account {
   a := &account{
      Name:      name,
      Password:  password,
      State:     0,
      LastLogin: time.Now().UnixNano(),
   }

   db.Create(a)

   return makeAccount(a)
}

func UpdateState(db *gorm.DB, id uint32, state byte) {
   db.Model(&account{ID: id}).Updates(&account{State: state, LastLogin: time.Now().UnixNano()})
}

func makeAccount(a *account) domain.Account {
   return domain.NewAccountBuilder(a.ID).
      SetName(a.Name).
      SetPassword(a.Password).
      Build()
}
