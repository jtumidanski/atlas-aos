package rest

import (
	account2 "atlas-aos/rest/account"
	"atlas-aos/rest/login"
	"github.com/gorilla/mux"
	"gorm.io/gorm"
	"log"
	"net/http"
	"os"
	"time"
)

type Server struct {
	l  *log.Logger
	hs *http.Server
}

func NewServer(l *log.Logger, db *gorm.DB) *Server {
	router := mux.NewRouter().PathPrefix("/ms/aos").Subrouter()
	router.Use(commonHeader)

	lRouter := router.PathPrefix("/logins").Subrouter()
	lRouter.HandleFunc("/", login.CreateLogin(l, db)).Methods(http.MethodPost)

	aRouter := router.PathPrefix("/accounts").Subrouter()
	aRouter.HandleFunc("/", account2.GetAccountByName(l, db)).Queries("name", "{name}").Methods(http.MethodGet)
	aRouter.HandleFunc("/{accountId}", account2.GetAccountById(l, db)).Methods(http.MethodGet)

	hs := http.Server{
		Addr:         ":8080",
		Handler:      router,
		ErrorLog:     l,                 // set the logger for the server
		ReadTimeout:  5 * time.Second,   // max time to read request from the client
		WriteTimeout: 10 * time.Second,  // max time to write response to the client
		IdleTimeout:  120 * time.Second, // max time for connections using TCP Keep-Alive
	}
	return &Server{l, &hs}
}

func (s *Server) Run() {
	s.l.Println("[INFO] Starting server on port 8080")
	err := s.hs.ListenAndServe()
	if err != nil {
		s.l.Printf("Error starting server: %s\n", err)
		os.Exit(1)
	}
}

func commonHeader(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Add("Content-Type", "application/json")
		next.ServeHTTP(w, r)
	})
}
