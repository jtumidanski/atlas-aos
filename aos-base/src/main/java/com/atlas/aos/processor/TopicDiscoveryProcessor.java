package com.atlas.aos.processor;

import com.atlas.shared.rest.RestService;
import com.atlas.shared.rest.UriBuilder;
import com.atlas.tds.rest.attribute.TopicAttributes;
import rest.DataBody;
import rest.DataContainer;

public final class TopicDiscoveryProcessor {
   private TopicDiscoveryProcessor() {
   }

   public static String getTopic(String id) {
      return UriBuilder.service(RestService.TOPIC_DISCOVERY)
            .pathParam("topics", id)
            .getRestClient(TopicAttributes.class)
            .retryOnFailure(1000)
            .getWithResponse()
            .result()
            .map(DataContainer::getData)
            .map(DataBody::getAttributes)
            .map(TopicAttributes::name)
            .orElseThrow();
   }
}