package com.atlas.aos;

import java.net.URI;

import com.atlas.shared.rest.RestServerFactory;
import com.atlas.shared.rest.RestService;
import com.atlas.shared.rest.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;

import database.PersistenceManager;

public class Server {
   public static void main(String[] args) {
      PersistenceManager.construct("atlas-aos");
      URI uri = UriBuilder.host(RestService.ACCOUNT).uri();
      final HttpServer server = RestServerFactory.create(uri, "com.atlas.aos.rest");
   }
}
