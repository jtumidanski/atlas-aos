package com.atlas.aos.builder;

import com.app.common.builder.RecordBuilder;
import com.atlas.aos.attribute.HwidAccountAttributes;

import builder.AttributeResultBuilder;

public class HwidAccountAttributesBuilder extends RecordBuilder<HwidAccountAttributes, HwidAccountAttributesBuilder>
      implements AttributeResultBuilder {
   private Integer accountId;

   private String hwid;

   private Integer relevance;

   @Override
   public HwidAccountAttributes construct() {
      return new HwidAccountAttributes(accountId, hwid, relevance);
   }

   @Override
   public HwidAccountAttributesBuilder getThis() {
      return this;
   }

   public HwidAccountAttributesBuilder setAccountId(Integer accountId) {
      this.accountId = accountId;
      return getThis();
   }

   public HwidAccountAttributesBuilder setHwid(String hwid) {
      this.hwid = hwid;
      return getThis();
   }

   public HwidAccountAttributesBuilder setRelevance(Integer relevance) {
      this.relevance = relevance;
      return getThis();
   }
}
