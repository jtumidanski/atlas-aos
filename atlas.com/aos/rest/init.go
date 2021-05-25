package rest

import (
	account2 "atlas-aos/rest/account"
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
		aRouter.HandleFunc("/", account2.GetAccountByName(l, db)).Queries("name", "{name}").Methods(http.MethodGet)
		aRouter.HandleFunc("/{accountId}", account2.GetAccountById(l, db)).Methods(http.MethodGet)

		return router
	}
}
