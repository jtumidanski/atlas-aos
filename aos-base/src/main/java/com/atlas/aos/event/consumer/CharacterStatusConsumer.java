package com.atlas.aos.event.consumer;

import com.atlas.aos.LoginState;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.csrv.constant.EventConstants;
import com.atlas.csrv.event.CharacterStatusEvent;
import com.atlas.kafka.consumer.SimpleEventHandler;

public class CharacterStatusConsumer implements SimpleEventHandler<CharacterStatusEvent> {
   @Override
   public void handle(Long aLong, CharacterStatusEvent event) {
      switch (event.type()) {
         case LOGIN -> AccountProcessor.getInstance().updateLoggedInStatus(event.accountId(), LoginState.LOGGED_IN);
         case LOGOUT -> AccountProcessor.getInstance().updateLoggedInStatus(event.accountId(), LoginState.NOT_LOGGED_IN);
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
      return System.getenv(EventConstants.TOPIC_CHARACTER_STATUS);
   }
}
