package com.atlas.aos.event.consumer;

import com.atlas.aos.LoginState;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.csrv.event.CharacterLoggedInEvent;
import com.atlas.kafka.consumer.ConsumerRecordHandler;

public class CharacterLoggedInConsumer implements ConsumerRecordHandler<Long, CharacterLoggedInEvent> {
   @Override
   public void handle(Long aLong, CharacterLoggedInEvent event) {
      AccountProcessor.getInstance().updateLoggedInStatus(event.accountId(), LoginState.LOGGED_IN);
   }
}
