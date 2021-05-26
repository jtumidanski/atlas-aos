package account

import (
	"atlas-aos/json"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

func GetAccountByName(l logrus.FieldLogger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		name := getAccountName(r)
		as := GetByName(db, name)
		if len(as) == 0 {
			rw.WriteHeader(http.StatusNotFound)
			return
		}

		a := as[0]

		rw.WriteHeader(http.StatusOK)
		result := DataContainer{
			Data: DataBody{
				Id:   strconv.FormatUint(uint64(a.Id()), 10),
				Type: "",
				Attributes: Attributes{
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

		err := json.ToJSON(result, rw)
		if err != nil {
			l.WithError(err).Errorf("Error writing response.")
		}
	}
}

func GetAccountById(l logrus.FieldLogger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		id := getAccountId(l)(r)
		a, err := GetById(db, id)
		if err != nil {
			rw.WriteHeader(http.StatusNotFound)
			return
		}

		rw.WriteHeader(http.StatusOK)
		result := DataContainer{
			Data: DataBody{
				Id:   strconv.FormatUint(uint64(a.Id()), 10),
				Type: "",
				Attributes: Attributes{
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

func getAccountName(r *http.Request) string {
	vars := mux.Vars(r)
	return vars["name"]
}

func getAccountId(l logrus.FieldLogger) func(r *http.Request) uint32 {
	return func(r *http.Request) uint32 {
		vars := mux.Vars(r)
		value, err := strconv.Atoi(vars["accountId"])
		if err != nil {
			l.WithError(err).Errorln("Error parsing characterId as uint32")
			return 0
		}
		return uint32(value)
	}
}
