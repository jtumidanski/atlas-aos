package account

import (
	"atlas-aos/json"
	"atlas-aos/rest"
	"github.com/gorilla/mux"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

const (
	getAccountByName = "get_account_by_name"
	getAccountById   = "get_account"
)

func InitResource(router *mux.Router, l logrus.FieldLogger, db *gorm.DB) {
	r := router.PathPrefix("/accounts").Subrouter()
	r.HandleFunc("/", registerGetAccountByName(l, db)).Queries("name", "{name}").Methods(http.MethodGet)
	r.HandleFunc("/{accountId}", registerGetAccountById(l, db)).Methods(http.MethodGet)
}

func registerGetAccountByName(l logrus.FieldLogger, db *gorm.DB) http.HandlerFunc {
	return rest.RetrieveSpan(getAccountByName, func(span opentracing.Span) http.HandlerFunc {
		return parseName(l, func(name string) http.HandlerFunc {
			return handleGetAccountByName(l, db)(span)(name)
		})
	})
}

type nameHandler func(name string) http.HandlerFunc

func parseName(l logrus.FieldLogger, next nameHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if val, ok := mux.Vars(r)["name"]; ok {
			next(val)(w, r)
		} else {
			l.Errorf("Missing name parameter.")
			w.WriteHeader(http.StatusBadRequest)
		}
	}
}

func handleGetAccountByName(l logrus.FieldLogger, db *gorm.DB) func(span opentracing.Span) func(name string) http.HandlerFunc {
	return func(span opentracing.Span) func(name string) http.HandlerFunc {
		return func(name string) http.HandlerFunc {
			return func(rw http.ResponseWriter, r *http.Request) {
				a, err := GetByName(l, db)(name)
				if err != nil {
					rw.WriteHeader(http.StatusNotFound)
					return
				}

				rw.WriteHeader(http.StatusOK)
				result := dataContainer{
					Data: dataBody{
						Id:   strconv.FormatUint(uint64(a.Id()), 10),
						Type: "",
						Attributes: attributes{
							Name:           a.Name(),
							Password:       a.Password(),
							Pin:            "",
							Pic:            "",
							LoggedIn:       a.State(),
							LastLogin:      0,
							Gender:         0,
							Banned:         false,
							TOS:            false,
							Language:       "",
							Country:        "",
							CharacterSlots: 4,
						},
					},
				}

				err = json.ToJSON(result, rw)
				if err != nil {
					l.WithError(err).Errorf("Error writing response.")
				}
			}
		}
	}
}

type idHandler func(id uint32) http.HandlerFunc

func parseId(l logrus.FieldLogger, next idHandler) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		vars := mux.Vars(r)
		value, err := strconv.Atoi(vars["accountId"])
		if err != nil {
			l.WithError(err).Errorln("Error parsing id as uint32")
			w.WriteHeader(http.StatusBadRequest)
			return
		}
		next(uint32(value))(w, r)
	}
}

func registerGetAccountById(l logrus.FieldLogger, db *gorm.DB) http.HandlerFunc {
	return rest.RetrieveSpan(getAccountById, func(span opentracing.Span) http.HandlerFunc {
		return parseId(l, func(id uint32) http.HandlerFunc {
			return handleGetAccountById(l, db)(span)(id)
		})
	})
}

func handleGetAccountById(l logrus.FieldLogger, db *gorm.DB) func(span opentracing.Span) func(id uint32) http.HandlerFunc {
	return func(span opentracing.Span) func(id uint32) http.HandlerFunc {
		return func(id uint32) http.HandlerFunc {
			return func(rw http.ResponseWriter, r *http.Request) {
				a, err := GetById(l, db)(id)
				if err != nil {
					rw.WriteHeader(http.StatusNotFound)
					return
				}

				rw.WriteHeader(http.StatusOK)
				result := dataContainer{
					Data: dataBody{
						Id:   strconv.FormatUint(uint64(a.Id()), 10),
						Type: "",
						Attributes: attributes{
							Name:           a.Name(),
							Password:       a.Password(),
							Pin:            "",
							Pic:            "",
							LoggedIn:       a.State(),
							LastLogin:      0,
							Gender:         0,
							Banned:         false,
							TOS:            false,
							Language:       "",
							Country:        "",
							CharacterSlots: 4,
						},
					},
				}

				err = json.ToJSON(result, rw)
				if err != nil {
					l.WithError(err).Errorln("Error writing response.")
				}
			}
		}
	}
}
