package com.atlas.aos.rest.processor;

import builder.ResultBuilder;
import com.app.rest.util.stream.Mappers;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.aos.rest.ResultObjectFactory;

import javax.ws.rs.core.Response;

public final class AccountRequestProcessor {
   private AccountRequestProcessor(){
   }

   public static ResultBuilder getAccounts(String name) {
      if (name != null) {
         return AccountProcessor.getAccountByName(name)
               .map(ResultObjectFactory::create)
               .map(Mappers::singleOkResult)
               .orElse(new ResultBuilder(Response.Status.NOT_FOUND));
      }
      return new ResultBuilder(Response.Status.NOT_FOUND);
   }

   public static ResultBuilder getAccount(int accountId) {
      return AccountProcessor.getAccountById(accountId)
            .map(ResultObjectFactory::create)
            .map(Mappers::singleOkResult)
            .orElse(new ResultBuilder(Response.Status.NOT_FOUND));
   }
}
