package login

import (
	"atlas-aos/json"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"net/http"
)

// GenericError is a generic error message returned by a server
type GenericError struct {
	Message string `json:"message"`
}

type ErrorListDataContainer struct {
	Errors []ErrorData `json:"errors"`
}

type ErrorData struct {
	Status int               `json:"status"`
	Code   string            `json:"code"`
	Title  string            `json:"title"`
	Detail string            `json:"detail"`
	Meta   map[string]string `json:"meta"`
}

func CreateLogin(l logrus.FieldLogger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		li := &InputDataContainer{}
		err := json.FromJSON(li, r.Body)
		if err != nil {
			l.WithError(err).Errorln("Deserializing instruction", err)
			rw.WriteHeader(http.StatusBadRequest)
			err = json.ToJSON(&GenericError{Message: err.Error()}, rw)
			if err != nil {
				l.WithError(err).Errorf("Writing error.")
			}
			return
		}

		att := li.Data.Attributes
		err = AttemptLogin(l, db, att.SessionId, att.Name, att.Password)
		if err != nil {
			l.WithError(err).Warnf("Login attempt by %s failed. error = %s", att.Name, err.Error())
			rw.WriteHeader(http.StatusForbidden)
			errorData := &ErrorListDataContainer{
				Errors: []ErrorData{
					{
						Status: 0,
						Code:   err.Error(),
						Title:  "",
						Detail: "",
						Meta:   nil,
					},
				},
			}

			err := json.ToJSON(errorData, rw)
			if err != nil {
				l.WithError(err).Errorln("Writing error.")
			}
			return
		}

		rw.WriteHeader(http.StatusNoContent)
	}
}
