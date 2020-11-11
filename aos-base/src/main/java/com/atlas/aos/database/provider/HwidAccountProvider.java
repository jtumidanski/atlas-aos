package com.atlas.aos.database.provider;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.atlas.aos.database.transformer.HwidRelevanceTransformer;
import com.atlas.aos.entity.HwidAccount;
import com.atlas.aos.model.HwidAccountData;

import accessor.AbstractQueryExecutor;

public class HwidAccountProvider extends AbstractQueryExecutor {
   private static HwidAccountProvider instance;

   public static HwidAccountProvider getInstance() {
      if (instance == null) {
         instance = new HwidAccountProvider();
      }
      return instance;
   }

   private HwidAccountProvider() {
   }

   public List<HwidAccountData> getHwids(EntityManager entityManager, int accountId) {
      TypedQuery<HwidAccount> query = entityManager.createQuery("SELECT a FROM HwidAccount a WHERE a.accountId = :accountId",
            HwidAccount.class);
      query.setParameter("accountId", accountId);
      return getResultList(query, new HwidRelevanceTransformer());
   }

   public Optional<HwidAccountData> getHwids(EntityManager entityManager, int accountId, String hwid) {
      TypedQuery<HwidAccount> query = entityManager.createQuery("SELECT a FROM HwidAccount a WHERE a.accountId = :accountId AND a"
            + ".hwid = :hwid", HwidAccount.class);
      query.setParameter("accountId", accountId);
      query.setParameter("hwid", hwid);
      return getSingleOptional(query, new HwidRelevanceTransformer());
   }

   public Optional<HwidAccountData> getById(EntityManager entityManager, Integer id) {
      TypedQuery<HwidAccount> query = entityManager.createQuery("SELECT a FROM HwidAccount a WHERE a.id = :id", HwidAccount.class);
      query.setParameter("id", id);
      return getSingleOptional(query, new HwidRelevanceTransformer());
   }
}