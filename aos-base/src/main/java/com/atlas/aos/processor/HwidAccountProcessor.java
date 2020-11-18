package com.atlas.aos.processor;

import javax.ws.rs.core.Response;

import com.atlas.aos.attribute.HwidAccountAttributes;
import com.atlas.aos.builder.HwidAccountAttributesBuilder;
import com.atlas.aos.database.administrator.AccountAdministrator;
import com.atlas.aos.database.administrator.HwidAccountAdministrator;
import com.atlas.aos.database.provider.AccountProvider;
import com.atlas.aos.database.provider.HwidAccountProvider;
import com.atlas.aos.model.HwidAccountData;

import builder.ResultBuilder;
import builder.ResultObjectBuilder;
import database.DatabaseConnection;

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

   public ResultBuilder getAll(Integer accountId, String hwid) {
      ResultBuilder resultBuilder = new ResultBuilder(Response.Status.NOT_IMPLEMENTED);
      if (accountId != null) {
         if (hwid != null) {
            DatabaseConnection.getInstance().withConnection(entityManager ->
                  HwidAccountProvider.getInstance().getHwids(entityManager, accountId, hwid)
                        .map(this::produceResult)
                        .ifPresent(result -> {
                           resultBuilder.setStatus(Response.Status.OK);
                           resultBuilder.addData(result);
                        }));
         } else {
            DatabaseConnection.getInstance().withConnection(entityManager ->
                  HwidAccountProvider.getInstance().getHwids(entityManager, accountId).stream()
                        .map(this::produceResult)
                        .forEach(result -> {
                           resultBuilder.setStatus(Response.Status.OK);
                           resultBuilder.addData(result);
                        }));
         }
      }
      return resultBuilder;
   }

   protected ResultObjectBuilder produceResult(HwidAccountData data) {
      return new ResultObjectBuilder(HwidAccountAttributes.class, data.id())
            .setAttribute(new HwidAccountAttributesBuilder()
                  .setAccountId(data.accountId())
                  .setHwid(data.hwid())
                  .setRelevance(data.relevance())
            );
   }

   public ResultBuilder update(Integer id, HwidAccountAttributes attributes) {
      ResultBuilder resultBuilder = new ResultBuilder(Response.Status.NOT_FOUND);

      DatabaseConnection.getInstance().withConnection(entityManager ->
            HwidAccountProvider.getInstance().getById(entityManager, id).ifPresent(data -> {
                     resultBuilder.setStatus(Response.Status.OK);
                        HwidAccountAdministrator.getInstance().update(entityManager, id, attributes.relevance());
                  }
            ));
      return resultBuilder;
   }
}
