package domain

type Account struct {
	id       uint32
	name     string
	password string
	state    byte
	banned   bool
}

func (a Account) Id() uint32 {
	return a.id
}

func (a Account) Name() string {
	return a.name
}

func (a Account) Password() string {
	return a.password
}

func (a *Account) Banned() bool {
	return a.banned
}

func (a Account) State() byte {
	return a.state
}

type accountBuilder struct {
	id       uint32
	name     string
	password string
	state    byte
	banned   bool
}

func NewAccountBuilder(id uint32) *accountBuilder {
	return &accountBuilder{id: id}
}

func (a *accountBuilder) SetName(name string) *accountBuilder {
	a.name = name
	return a
}

func (a *accountBuilder) SetPassword(password string) *accountBuilder {
	a.password = password
	return a
}

func (a *accountBuilder) SetState(state byte) *accountBuilder {
	a.state = state
	return a
}

func (a *accountBuilder) Build() Account {
	return Account{
		id:       a.id,
		name:     a.name,
		password: a.password,
		state:    a.state,
		banned:   a.banned,
	}
}
