package domain

type ModelProvider[M any] func() (M, error)

type ModelListProvider[M any] func() ([]M, error)
