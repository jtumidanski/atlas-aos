package com.atlas.aos.database.provider;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.app.database.provider.NamedQueryClient;
import com.atlas.aos.database.transformer.HwidRelevanceTransformer;
import com.atlas.aos.entity.HwidAccount;
import com.atlas.aos.model.HwidAccountData;

public class HwidAccountProvider {
   private HwidAccountProvider() {
   }

   public static List<HwidAccountData> getHwids(EntityManager entityManager, int accountId) {
      return new NamedQueryClient<>(entityManager, HwidAccount.GET_BY_ACCOUNT_ID, HwidAccount.class)
            .setParameter(HwidAccount.ACCOUNT_ID, accountId)
            .list(new HwidRelevanceTransformer());
   }

   public static Optional<HwidAccountData> getHwids(EntityManager entityManager, int accountId, String hwid) {
      return new NamedQueryClient<>(entityManager, HwidAccount.GET_BY_ACCOUNT_ID_AND_HWID, HwidAccount.class)
            .setParameter(HwidAccount.ACCOUNT_ID, accountId)
            .setParameter(HwidAccount.HWID, hwid)
            .element(new HwidRelevanceTransformer());
   }

   public static Optional<HwidAccountData> getById(EntityManager entityManager, Integer id) {
      return new NamedQueryClient<>(entityManager, HwidAccount.GET_BY_ID, HwidAccount.class)
            .setParameter(HwidAccount.ID, id)
            .element(new HwidRelevanceTransformer());
   }
}