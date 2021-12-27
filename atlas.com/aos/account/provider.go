package account

import (
	"atlas-aos/database"
	"gorm.io/gorm"
)

func entityById(id uint32) database.EntityProvider[entity] {
	return func(db *gorm.DB) (entity, error) {
		var result entity
		err := db.First(&result, id).Error
		return result, err
	}
}

func entitiesByName(name string) database.EntityListProvider[entity] {
	return func(db *gorm.DB) ([]entity, error) {
		var results []entity
		err := db.Where(&entity{Name: name}).First(&results).Error
		return results, err
	}
}
