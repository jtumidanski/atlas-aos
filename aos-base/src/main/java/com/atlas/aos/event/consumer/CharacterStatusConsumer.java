package com.atlas.aos.event.consumer;

import com.atlas.aos.model.LoginState;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.aos.processor.TopicDiscoveryProcessor;
import com.atlas.csrv.constant.EventConstants;
import com.atlas.csrv.event.CharacterStatusEvent;
import com.atlas.kafka.consumer.SimpleEventHandler;

public class CharacterStatusConsumer implements SimpleEventHandler<CharacterStatusEvent> {
   @Override
   public void handle(Long key, CharacterStatusEvent event) {
      switch (event.type()) {
         case LOGIN -> AccountProcessor.updateLoggedInStatus(event.accountId(), LoginState.LOGGED_IN);
         case LOGOUT -> AccountProcessor.updateLoggedInStatus(event.accountId(), LoginState.NOT_LOGGED_IN);
      }
   }

   @Override
   public Class<CharacterStatusEvent> getEventClass() {
      return CharacterStatusEvent.class;
   }

   @Override
   public String getConsumerId() {
      return "Account Service";
   }

   @Override
   public String getBootstrapServers() {
      return System.getenv("BOOTSTRAP_SERVERS");
   }

   @Override
   public String getTopic() {
      return TopicDiscoveryProcessor.getTopic(EventConstants.TOPIC_CHARACTER_STATUS);
   }
}
