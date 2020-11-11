package com.atlas.aos.builder;

import com.atlas.aos.attribute.HwidAccountAttributes;

import builder.AttributeResultBuilder;
import builder.Builder;

public class HwidAccountAttributesBuilder extends Builder<HwidAccountAttributes, HwidAccountAttributesBuilder>
      implements AttributeResultBuilder {
   @Override
   public HwidAccountAttributes construct() {
      return new HwidAccountAttributes();
   }

   @Override
   public HwidAccountAttributesBuilder getThis() {
      return this;
   }

   public HwidAccountAttributesBuilder setAccountId(Integer accountId) {
      return add(attr -> attr.setAccountId(accountId));
   }

   public HwidAccountAttributesBuilder setHwid(String hwid) {
      return add(attr -> attr.setHwid(hwid));
   }

   public HwidAccountAttributesBuilder setRelevance(Integer relevance) {
      return add(attr -> attr.setRelevance(relevance));
   }
}
