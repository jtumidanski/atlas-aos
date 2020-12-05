package com.atlas.aos;

import java.net.URI;

import com.atlas.aos.event.consumer.CharacterStatusConsumer;
import com.atlas.kafka.consumer.SimpleEventConsumerFactory;
import com.atlas.shared.rest.RestServerFactory;
import com.atlas.shared.rest.RestService;
import com.atlas.shared.rest.UriBuilder;

import database.PersistenceManager;

public class Server {
   public static void main(String[] args) {
      PersistenceManager.construct("atlas-aos");

      SimpleEventConsumerFactory.create(new CharacterStatusConsumer());

      URI uri = UriBuilder.host(RestService.ACCOUNT).uri();
      RestServerFactory.create(uri, "com.atlas.aos.rest");
   }
}
