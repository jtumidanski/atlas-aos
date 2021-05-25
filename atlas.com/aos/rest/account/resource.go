package attributes

import (
	account2 "atlas-aos/account"
	"atlas-aos/json"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"log"
	"net/http"
	"strconv"
)

func GetAccountByName(_ logrus.FieldLogger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		name := getAccountName(r)
		as := account2.GetByName(db, name)
		if len(as) == 0 {
			rw.WriteHeader(http.StatusNotFound)
			return
		}

		account := as[0]

		rw.WriteHeader(http.StatusOK)
		a := AccountDataContainer{
			Data: AccountData{
				Id:   strconv.FormatUint(uint64(account.Id()), 10),
				Type: "",
				Attributes: AccountAttributes{
					Name:           account.Name(),
					Password:       account.Password(),
					Pin:            "",
					Pic:            "",
					LoggedIn:       account.State(),
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

		json.ToJSON(a, rw)
	}
}

func GetAccountById(_ logrus.FieldLogger, db *gorm.DB) func(http.ResponseWriter, *http.Request) {
	return func(rw http.ResponseWriter, r *http.Request) {
		id := getAccountId(r)
		a, err := account2.GetById(db, id)
		if err != nil {
			rw.WriteHeader(http.StatusNotFound)
			return
		}

		rw.WriteHeader(http.StatusOK)
		result := AccountDataContainer{
			Data: AccountData{
				Id:   strconv.FormatUint(uint64(a.Id()), 10),
				Type: "",
				Attributes: AccountAttributes{
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

		json.ToJSON(result, rw)
	}
}

func getAccountName(r *http.Request) string {
	vars := mux.Vars(r)
	return vars["name"]
}

func getAccountId(r *http.Request) uint32 {
	vars := mux.Vars(r)
	value, err := strconv.Atoi(vars["accountId"])
	if err != nil {
		log.Println("Error parsing characterId as uint32")
		return 0
	}
	return uint32(value)
}
