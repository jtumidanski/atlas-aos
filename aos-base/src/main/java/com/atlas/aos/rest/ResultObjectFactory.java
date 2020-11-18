package com.atlas.aos.rest;

import com.atlas.aos.attribute.AccountAttributes;
import com.atlas.aos.attribute.HwidAccountAttributes;
import com.atlas.aos.builder.AccountAttributesBuilder;
import com.atlas.aos.builder.HwidAccountAttributesBuilder;
import com.atlas.aos.model.AccountData;
import com.atlas.aos.model.HwidAccountData;

import builder.ResultObjectBuilder;

public class ResultObjectFactory {
   public static ResultObjectBuilder create(HwidAccountData data) {
      return new ResultObjectBuilder(HwidAccountAttributes.class, data.id())
            .setAttribute(new HwidAccountAttributesBuilder()
                  .setAccountId(data.accountId())
                  .setHwid(data.hwid())
                  .setRelevance(data.relevance())
            );
   }

   public static ResultObjectBuilder create(AccountData accountData) {
      return new ResultObjectBuilder(AccountAttributes.class, accountData.id())
            .setAttribute(new AccountAttributesBuilder()
                  .setName(accountData.name())
                  .setPassword(accountData.password())
                  .setPin(accountData.pin())
                  .setPic(accountData.pic())
                  .setLoggedIn(accountData.loggedIn())
                  .setLastLogin(accountData.lastLogin())
                  .setGender((byte) accountData.gender())
                  .setBanned(accountData.banned())
                  .setTos(accountData.tos())
                  .setLanguage(accountData.language())
                  .setCountry(accountData.country())
                  .setCharacterSlots(accountData.characterSlots())
            );
   }
}
