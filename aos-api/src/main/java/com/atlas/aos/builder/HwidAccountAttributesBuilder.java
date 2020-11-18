package com.atlas.aos.builder;

import builder.AttributeResultBuilder;
import builder.RecordBuilder;
import com.atlas.aos.attribute.HwidAccountAttributes;

public class HwidAccountAttributesBuilder extends RecordBuilder<HwidAccountAttributes, HwidAccountAttributesBuilder> implements AttributeResultBuilder {
   private static final String ACCOUNT_ID = "ACCOUNT_ID";

   private static final String HWID = "HWID";

   private static final String RELEVANCE = "RELEVANCE";


   @Override
   public HwidAccountAttributes construct() {
      return new HwidAccountAttributes(get(ACCOUNT_ID), get(HWID), get(RELEVANCE));
   }

   @Override
   public HwidAccountAttributesBuilder getThis() {
      return this;
   }

   public HwidAccountAttributesBuilder setAccountId(int accountId) {
      return set(ACCOUNT_ID, accountId);
   }

   public HwidAccountAttributesBuilder setHwid(String hwid) {
      return set(HWID, hwid);
   }

   public HwidAccountAttributesBuilder setRelevance(int relevance) {
      return set(RELEVANCE, relevance);
   }

}
