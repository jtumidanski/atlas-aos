package domain

import "errors"

type Filter[M any] func(m M) bool

type Transformer[M any, A any] func(a A) (m M)

type IdOperator func(uint32) error

type IdListOperator func([]uint32) error

type ModelOperator[M any] func(M) error

type ModelListOperator[M any] func([]M) error

func For[M any](p ModelProvider[M], f ModelOperator[M]) error {
	m, err := p()
	if err != nil {
		return err
	}
	return f(m)
}

func ForEach[M any](p ModelListProvider[M], f ModelOperator[M]) error {
	ms, err := p()
	if err != nil {
		return err
	}
	for _, m := range ms {
		err = f(m)
		if err != nil {
			return err
		}
	}
	return nil
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
