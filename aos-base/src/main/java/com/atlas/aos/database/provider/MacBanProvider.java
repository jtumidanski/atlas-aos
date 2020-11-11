package com.atlas.aos.database.provider;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import accessor.AbstractQueryExecutor;

public class MacBanProvider extends AbstractQueryExecutor {
   private static MacBanProvider instance;

   public static MacBanProvider getInstance() {
      if (instance == null) {
         instance = new MacBanProvider();
      }
      return instance;
   }

   private MacBanProvider() {
   }

   public int getMacBanCount(EntityManager entityManager, List<String> macs) {
      TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(*) FROM MacBan m WHERE m.mac IN :macs", Long.class);
      query.setParameter("macs", macs);
      return getSingleWithDefault(query, 0L).intValue();
   }
}
