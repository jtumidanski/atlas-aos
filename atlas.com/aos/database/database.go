package database

import (
	account2 "atlas-aos/account"
	"atlas-aos/retry"
	"github.com/sirupsen/logrus"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func ConnectToDatabase(l logrus.FieldLogger) *gorm.DB {
	var db *gorm.DB

	tryToConnect := func(attempt int) (bool, error) {
		var err error
		db, err = gorm.Open(mysql.New(mysql.Config{
			DSN:                       "root:the@tcp(atlas-db:3306)/atlas-aos?charset=utf8&parseTime=True&loc=Local",
			DefaultStringSize:         256,
			DisableDatetimePrecision:  true,
			DontSupportRenameIndex:    true,
			DontSupportRenameColumn:   true,
			SkipInitializeWithVersion: false,
		}), &gorm.Config{})
		if err != nil {
			return true, err
		}
		return false, err
	}

	err := retry.Try(tryToConnect, 10)
	if err != nil {
		l.WithError(err).Fatalf("Failed to connect to database.")
	}

	// Migrate the schema
	account2.Migration(db)
	return db
}