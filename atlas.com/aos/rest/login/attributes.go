package login

import "atlas-aos/rest"

type LoginDataContainer struct {
   data     rest.DataSegment
   included rest.DataSegment
}

type LoginData struct {
   Id         string            `json:"id"`
   Type       string            `json:"type"`
   Attributes LoginAttributes `json:"attributes"`
}

type LoginAttributes struct {
   Name           string `json:"name"`
   Password       string `json:"password"`
   Pin            string `json:"pin"`
   Pic            string `json:"pic"`
   LoggedIn       int    `json:"loggedIn"`
   LastLogin      uint64 `json:"lastLogin"`
   Gender         byte   `json:"gender"`
   Banned         bool   `json:"banned"`
   TOS            bool   `json:"tos"`
   Language       string `json:"language"`
   Country        string `json:"country"`
   CharacterSlots int16  `json:"characterSlots"`
}

func (a *LoginDataContainer) UnmarshalJSON(data []byte) error {
   d, i, err := rest.UnmarshalRoot(data, rest.MapperFunc(EmptyLoginData))
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
