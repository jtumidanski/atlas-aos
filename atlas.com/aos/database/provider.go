package database

import (
   "atlas-aos/domain"
   "gorm.io/gorm"
)

type EntityProvider[E any] func(db *gorm.DB) (E, error)

type EntityListProvider[E any] func(db *gorm.DB) ([]E, error)

func ModelProvider[M any, E any](db *gorm.DB) func(ep EntityProvider[E], t domain.Transformer[M, E]) domain.ModelProvider[M] {
   return func(ep EntityProvider[E], t domain.Transformer[M, E]) domain.ModelProvider[M] {
      return func() (M, error) {
         var r M
         e, err := ep(db)
         if err != nil {
            return r, err
         }
         r = t(e)
         return r, nil
      }
   }
}

func ModelListProvider[M any, E any](db *gorm.DB) func(ep EntityListProvider[E], t domain.Transformer[M, E]) domain.ModelListProvider[M] {
   return func(ep EntityListProvider[E], t domain.Transformer[M, E]) domain.ModelListProvider[M] {
      return func() ([]M, error) {
         var r []M
         es, err := ep(db)
         if err != nil {
            return r, err
         }
         return domain.Map[M, E](es, t), nil
      }
   }
}
