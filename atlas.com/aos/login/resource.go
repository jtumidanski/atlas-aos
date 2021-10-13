package login

import (
	"atlas-aos/json"
	"atlas-aos/rest"
	"atlas-aos/rest/resource"
	"github.com/gorilla/mux"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"net/http"
)

const (
	createLogin = "create_login"
)

type errorListDataContainer struct {
	Errors []errorData `json:"errors"`
}

type errorData struct {
	Status int               `json:"status"`
	Code   string            `json:"code"`
	Title  string            `json:"title"`
	Detail string            `json:"detail"`
	Meta   map[string]string `json:"meta"`
}

func InitResource(router *mux.Router, l logrus.FieldLogger, db *gorm.DB) {
	r := router.PathPrefix("/logins").Subrouter()
	r.HandleFunc("/", registerCreateLogin(l, db)).Methods(http.MethodPost)
}

func registerCreateLogin(l logrus.FieldLogger, db *gorm.DB) http.HandlerFunc {
	return rest.RetrieveSpan(createLogin, func(span opentracing.Span) http.HandlerFunc {
		return parseInput(l, func(container *inputDataContainer) http.HandlerFunc {
			return handleCreateLogin(l, db)(span)(container)
		})
	})
}

type inputHandler func(container *inputDataContainer) http.HandlerFunc

func parseInput(l logrus.FieldLogger, next inputHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		li := &inputDataContainer{}
		err := json.FromJSON(li, r.Body)
		if err != nil {
			l.WithError(err).Errorln("Deserializing input", err)
			w.WriteHeader(http.StatusBadRequest)
			err = json.ToJSON(&resource.GenericError{Message: err.Error()}, w)
			if err != nil {
				l.WithError(err).Errorf("Writing error.")
			}
			return
		}
		next(li)(w, r)
	}
}

func handleCreateLogin(l logrus.FieldLogger, db *gorm.DB) func(span opentracing.Span) func(container *inputDataContainer) http.HandlerFunc {
	return func(span opentracing.Span) func(container *inputDataContainer) http.HandlerFunc {
		return func(container *inputDataContainer) http.HandlerFunc {
			return func(rw http.ResponseWriter, r *http.Request) {
				att := container.Data.Attributes
				err := AttemptLogin(l, db)(att.SessionId, att.Name, att.Password)
				if err != nil {
					l.WithError(err).Warnf("Login attempt by %s failed. error = %s", att.Name, err.Error())
					rw.WriteHeader(http.StatusForbidden)
					errorData := &errorListDataContainer{
						Errors: []errorData{
							{
								Status: 0,
								Code:   err.Error(),
								Title:  "",
								Detail: "",
								Meta:   nil,
							},
						},
					}
					err = json.ToJSON(errorData, rw)
					if err != nil {
						l.WithError(err).Errorln("Writing error.")
					}
					return
				}

				rw.WriteHeader(http.StatusNoContent)
			}
		}
	}
}
