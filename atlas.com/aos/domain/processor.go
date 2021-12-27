package domain

import "errors"

type Filter[M any] func(m M) bool

type Transformer[M any, A any] func(a A) (m M)

type ModelOperator[M any] func(M)

type ModelListOperator[M any] func([]M)

func ForEach[M any](f ModelOperator[M]) ModelListOperator[M] {
   return func(ms []M) {
      for _, m := range ms {
         f(m)
      }
   }
}

func Map[M any, A any](as []A, t Transformer[M, A]) []M {
   var rs []M
   for _, a := range as {
      rs = append(rs, t(a))
   }
   return rs
}

func First[M any](ms []M, filters ...Filter[M]) (M, error) {
   var r M
   if len(filters) == 0 {
      return ms[0], nil
   }

   for _, m := range ms {
      ok := true
      for _, filter := range filters {
         if !filter(m) {
            ok = false
         }
      }
      if ok {
         return m, nil
      }
   }
   return r, errors.New("no result found")
}
