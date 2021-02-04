package login

import (
	"atlas-aos/processors"
	"atlas-aos/rest/json"
	"gorm.io/gorm"
	"log"
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

func CreateLogin(l *log.Logger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		li := &LoginInputContainer{}
		err := json.FromJSON(li, r.Body)
		if err != nil {
			l.Println("[ERROR] deserializing instruction", err)
			rw.WriteHeader(http.StatusBadRequest)
			json.ToJSON(&GenericError{Message: err.Error()}, rw)
			return
		}

		att := li.Data.Attributes
		err = processors.AttemptLogin(db, att.SessionId, att.Name, att.Password)
		if err != nil {
			l.Println("[WARN] login attempt by %s failed. error = %s", att.Name, err.Error())
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

			json.ToJSON(errorData, rw)
			return
		}

		rw.WriteHeader(http.StatusNoContent)
	}
}
