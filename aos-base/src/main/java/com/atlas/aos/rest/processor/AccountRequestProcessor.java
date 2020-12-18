package com.atlas.aos.rest.processor;

import com.app.rest.util.stream.Mappers;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.aos.rest.ResultObjectFactory;

import builder.ResultBuilder;

public final class AccountRequestProcessor {
   private AccountRequestProcessor() {
   }

   public static ResultBuilder getAccounts(String name) {
      if (name != null) {
         return AccountProcessor.getAccountByName(name)
               .map(ResultObjectFactory::create)
               .map(Mappers::singleOkResult)
               .orElseGet(ResultBuilder::notFound);
      }
      return ResultBuilder.notFound();
   }

   public static ResultBuilder getAccount(int accountId) {
      return AccountProcessor.getAccountById(accountId)
            .map(ResultObjectFactory::create)
            .map(Mappers::singleOkResult)
            .orElseGet(ResultBuilder::notFound);
   }
}
