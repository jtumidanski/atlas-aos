package login

import (
	"atlas-aos/rest/response"
)

type LoginDataContainer struct {
	data     response.DataSegment
	included response.DataSegment
}

type LoginInputContainer struct {
	Data LoginData `json:"data"`
}

type LoginData struct {
	Id         string          `json:"id"`
	Type       string          `json:"type"`
	Attributes LoginAttributes `json:"attributes"`
}

type LoginAttributes struct {
	SessionId uint32 `json:"sessionId"`
	Name      string `json:"name"`
	Password  string `json:"password"`
	IpAddress string `json:"ipAddress"`
	State     int    `json:"state"`
}

func (a *LoginDataContainer) UnmarshalJSON(data []byte) error {
	d, i, err := response.UnmarshalRoot(data, response.MapperFunc(EmptyLoginData))
	if err != nil {
		return err
	}

	a.data = d
	a.included = i
	return nil
}

func (a *LoginDataContainer) Data() *LoginData {
	if len(a.data) >= 1 {
		return a.data[0].(*LoginData)
	}
	return nil
}

func (a *LoginDataContainer) DataList() []LoginData {
	var r = make([]LoginData, 0)
	for _, x := range a.data {
		r = append(r, *x.(*LoginData))
	}
	return r
}

func EmptyLoginData() interface{} {
	return &LoginData{}
}
