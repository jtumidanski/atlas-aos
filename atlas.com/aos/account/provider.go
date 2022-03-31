package account

import (
	"atlas-aos/database"
	"atlas-aos/model"
	"gorm.io/gorm"
)

func entityById(id uint32) database.EntityProvider[entity] {
	return func(db *gorm.DB) model.Provider[entity] {
		var result entity
		err := db.First(&result, id).Error
		if err != nil {
			return model.ErrorProvider[entity](err)
		}
		return model.FixedProvider[entity](result)
	}
}

func entitiesByName(name string) database.EntitySliceProvider[entity] {
	return func(db *gorm.DB) model.SliceProvider[entity] {
		var results []entity
		err := db.Where(&entity{Name: name}).First(&results).Error
		if err != nil {
			return model.ErrorSliceProvider[entity](err)
		}
		return model.FixedSliceProvider[entity](results)
	}
}
