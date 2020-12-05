package com.atlas.aos.event.consumer;

import com.atlas.aos.LoginState;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.csrv.event.CharacterStatusEvent;
import com.atlas.kafka.consumer.ConsumerRecordHandler;

public class CharacterStatusConsumer implements ConsumerRecordHandler<Long, CharacterStatusEvent> {
   @Override
   public void handle(Long aLong, CharacterStatusEvent event) {
      switch (event.type()) {
         case LOGIN -> AccountProcessor.getInstance().updateLoggedInStatus(event.accountId(), LoginState.LOGGED_IN);
         case LOGOUT -> AccountProcessor.getInstance().updateLoggedInStatus(event.accountId(), LoginState.NOT_LOGGED_IN);
      }
   }
}
