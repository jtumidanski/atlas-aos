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

public class HwidAccountProcessor {
   private static final Object lock = new Object();

   private static volatile HwidAccountProcessor instance;

   public static HwidAccountProcessor getInstance() {
      HwidAccountProcessor result = instance;
      if (result == null) {
         synchronized (lock) {
            result = instance;
            if (result == null) {
               result = new HwidAccountProcessor();
               instance = result;
            }
         }
      }
      return result;
   }

   public ResultBuilder getByAccountIdAndHwid(int accountId, String hwid) {
      return Connection.instance()
            .element(entityManager -> HwidAccountProvider.getHwids(entityManager, accountId, hwid))
            .map(ResultObjectFactory::create)
            .map(Mappers::singleResult)
            .orElse(new ResultBuilder());
   }

   public ResultBuilder getByAccountId(int accountId) {
      return Connection.instance()
            .list(entityManager -> HwidAccountProvider.getHwids(entityManager, accountId))
            .stream()
            .map(ResultObjectFactory::create)
            .collect(Collectors.toResultBuilder());
   }

   public ResultBuilder getAll(Integer accountId, String hwid) {
      if (accountId != null) {
         if (hwid != null) {
            return getByAccountIdAndHwid(accountId, hwid);
         } else {
            return getByAccountId(accountId);
         }
      }
      return new ResultBuilder(Response.Status.NOT_IMPLEMENTED);
   }

   public ResultBuilder update(Integer id, HwidAccountAttributes attributes) {
      ResultBuilder resultBuilder = new ResultBuilder(Response.Status.NOT_FOUND);
      Connection.instance().with(entityManager -> {
         resultBuilder.setStatus(Response.Status.OK);
         HwidAccountAdministrator.update(entityManager, id, attributes.relevance());
      });
      return resultBuilder;
   }
}
