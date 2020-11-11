package com.atlas.aos.attribute;

import rest.AttributeResult;

public class HwidAccountAttributes implements AttributeResult {
   private Integer accountId;

   private String hwid;

   private Integer relevance;

   public Integer getAccountId() {
      return accountId;
   }

   public void setAccountId(Integer accountId) {
      this.accountId = accountId;
   }

   public String getHwid() {
      return hwid;
   }

   public void setHwid(String hwid) {
      this.hwid = hwid;
   }

   public Integer getRelevance() {
      return relevance;
   }

   public void setRelevance(Integer relevance) {
      this.relevance = relevance;
   }
}
