package com.atlas.aos.processor;

import javax.ws.rs.core.Response;

import com.app.rest.util.stream.Collectors;
import com.app.rest.util.stream.Mappers;
import com.atlas.aos.attribute.HwidAccountAttributes;
import com.atlas.aos.database.administrator.HwidAccountAdministrator;
import com.atlas.aos.database.provider.HwidAccountProvider;
import com.atlas.aos.rest.ResultObjectFactory;

import builder.ResultBuilder;
import database.Connection;

public final class HwidAccountProcessor {
   private HwidAccountProcessor() {
   }

   public static ResultBuilder getByAccountIdAndHwid(int accountId, String hwid) {
      return Connection.instance()
            .element(entityManager -> HwidAccountProvider.getHwids(entityManager, accountId, hwid))
            .map(ResultObjectFactory::create)
            .map(Mappers::singleOkResult)
            .orElseGet(ResultBuilder::notFound);
   }

   public static ResultBuilder getByAccountId(int accountId) {
      return Connection.instance()
            .list(entityManager -> HwidAccountProvider.getHwids(entityManager, accountId))
            .stream()
            .map(ResultObjectFactory::create)
            .collect(Collectors.toResultBuilder());
   }

   public static ResultBuilder getAll(Integer accountId, String hwid) {
      if (accountId != null) {
         if (hwid != null) {
            return getByAccountIdAndHwid(accountId, hwid);
         } else {
            return getByAccountId(accountId);
         }
      }
      return ResultBuilder.notImplemented();
   }

   public static ResultBuilder update(Integer id, HwidAccountAttributes attributes) {
      ResultBuilder resultBuilder = ResultBuilder.notFound();
      Connection.instance().with(entityManager -> {
         resultBuilder.setStatus(Response.Status.OK);
         HwidAccountAdministrator.update(entityManager, id, attributes.relevance());
      });
      return resultBuilder;
   }
}
