package rest

import (
	"atlas-aos/account"
	"atlas-aos/rest/login"
	"context"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
	"gorm.io/gorm"
	"net/http"
	"sync"
)

func CreateRestService(l *logrus.Logger, db *gorm.DB, ctx context.Context, wg *sync.WaitGroup) {
	go NewServer(l, ctx, wg, ProduceRoutes(db))
}

func ProduceRoutes(db *gorm.DB) func(l logrus.FieldLogger) http.Handler {
	return func(l logrus.FieldLogger) http.Handler {
		router := mux.NewRouter().PathPrefix("/ms/aos").Subrouter()
		router.Use(CommonHeader)

		lRouter := router.PathPrefix("/logins").Subrouter()
		lRouter.HandleFunc("/", login.CreateLogin(l, db)).Methods(http.MethodPost)

		aRouter := router.PathPrefix("/accounts").Subrouter()
		aRouter.HandleFunc("/", account.GetAccountByName(l, db)).Queries("name", "{name}").Methods(http.MethodGet)
		aRouter.HandleFunc("/{accountId}", account.GetAccountById(l, db)).Methods(http.MethodGet)

		return router
	}
}
