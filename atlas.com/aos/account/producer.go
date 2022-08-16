package account

import (
	"atlas-aos/kafka"
	"github.com/opentracing/opentracing-go"
	"github.com/sirupsen/logrus"
)

type statusEvent struct {
	AccountId uint32 `json:"account_id"`
	Name      string `json:"name"`
	Status    string `json:"status"`
}

func emitCreatedEvent(l logrus.FieldLogger, span opentracing.Span) func(accountId uint32, name string) {
	producer := kafka.ProduceEvent(l, span, "TOPIC_ACCOUNT_STATUS_EVENT")
	return func(accountId uint32, name string) {
		event := &statusEvent{
			AccountId: accountId,
			Name:      name,
			Status:    "CREATED",
		}
		producer(kafka.CreateKey(int(accountId)), event)
	}
}
