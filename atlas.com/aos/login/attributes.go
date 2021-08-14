package login

import (
	"atlas-aos/rest/response"
)

type DataContainer struct {
	data     response.DataSegment
	included response.DataSegment
}

type InputDataContainer struct {
	Data DataBody `json:"data"`
}

type DataBody struct {
	Id         string     `json:"id"`
	Type       string     `json:"type"`
	Attributes Attributes `json:"attributes"`
}

type Attributes struct {
	SessionId uint32 `json:"sessionId"`
	Name      string `json:"name"`
	Password  string `json:"password"`
	IpAddress string `json:"ipAddress"`
	State     int    `json:"state"`
}

func (a *DataContainer) UnmarshalJSON(data []byte) error {
	d, i, err := response.UnmarshalRoot(data, response.MapperFunc(EmptyLoginData))
	if err != nil {
		return err
	}

	a.data = d
	a.included = i
	return nil
}

func (a *DataContainer) Data() *DataBody {
	if len(a.data) >= 1 {
		return a.data[0].(*DataBody)
	}
	return nil
}

func (a *DataContainer) DataList() []DataBody {
	var r = make([]DataBody, 0)
	for _, x := range a.data {
		r = append(r, *x.(*DataBody))
	}
	return r
}

func EmptyLoginData() interface{} {
	return &DataBody{}
}
