package com.atlas.aos;

import java.net.URI;
import java.util.concurrent.Executors;

import com.atlas.aos.event.consumer.CharacterLoggedInConsumer;
import com.atlas.csrv.constant.EventConstants;
import com.atlas.csrv.event.CharacterLoggedInEvent;
import com.atlas.kafka.consumer.ConsumerBuilder;
import com.atlas.shared.rest.RestServerFactory;
import com.atlas.shared.rest.RestService;
import com.atlas.shared.rest.UriBuilder;

import database.PersistenceManager;

public class Server {
   public static void main(String[] args) {
      PersistenceManager.construct("atlas-aos");

      URI uri = UriBuilder.host(RestService.ACCOUNT).uri();
      RestServerFactory.create(uri, "com.atlas.aos.rest");

      Executors.newSingleThreadExecutor().execute(
            new ConsumerBuilder<>("Account Service", CharacterLoggedInEvent.class)
                  .setBootstrapServers(System.getenv("BOOTSTRAP_SERVERS"))
                  .setTopic(System.getenv(EventConstants.TOPIC_CHARACTER_LOGIN))
                  .setHandler(new CharacterLoggedInConsumer())
                  .build()
      );
   }
}
