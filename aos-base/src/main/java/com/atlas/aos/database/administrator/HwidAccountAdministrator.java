package com.atlas.aos.database.administrator;

import java.sql.Timestamp;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.atlas.aos.entity.HwidAccount;

import accessor.AbstractQueryExecutor;

public class HwidAccountAdministrator extends AbstractQueryExecutor {
   private static HwidAccountAdministrator instance;

   public static HwidAccountAdministrator getInstance() {
      if (instance == null) {
         instance = new HwidAccountAdministrator();
      }
      return instance;
   }

   private HwidAccountAdministrator() {
   }

   public void updateByAccountId(EntityManager entityManager, int accountId, String hwid, int relevance, Timestamp timestamp) {
      Query query = entityManager.createQuery(
            "UPDATE HwidAccount SET relevance = :relevance, expiresAt = :timestamp WHERE accountId = :accountId AND hwid LIKE :hwid");
      query.setParameter("relevance", relevance);
      query.setParameter("timestamp", timestamp);
      query.setParameter("accountId", accountId);
      query.setParameter("hwid", hwid);
      execute(entityManager, query);
   }

   public void create(EntityManager entityManager, int accountId, String hwid, Timestamp timestamp) {
      HwidAccount hwidAccount = new HwidAccount();
      hwidAccount.setAccountId(accountId);
      hwidAccount.setHwid(hwid);
      hwidAccount.setExpiresAt(timestamp);
      insert(entityManager, hwidAccount);
   }

   public void deleteExpired(EntityManager entityManager) {
      Query query = entityManager.createQuery("DELETE FROM HwidAccount WHERE expiresAt < :timestamp");
      query.setParameter("timestamp", new Timestamp(Calendar.getInstance().getTimeInMillis()));
      execute(entityManager, query);
   }

   public void update(EntityManager entityManager, int id, Integer relevance) {
      update(entityManager, HwidAccount.class, id, hwidAccount -> {
         if (relevance != null) {
            Timestamp nextTimestamp = new Timestamp(System.currentTimeMillis() + hwidExpirationUpdate(relevance - 1));
            if (relevance == Byte.MAX_VALUE) {
               hwidAccount.setRelevance(0);
            } else {
               hwidAccount.setRelevance(relevance);
            }
            hwidAccount.setExpiresAt(nextTimestamp);
         }
      });
   }

   private long hwidExpirationUpdate(int relevance) {
      int degree = 1;
      int i = relevance;
      int subDegree;
      while ((subDegree = 5 * degree) <= i) {
         i -= subDegree;
         degree++;
      }

      degree--;
      int baseTime, subDegreeTime;
      if (degree > 2) {
         subDegreeTime = 10;
      } else {
         subDegreeTime = 1 + (3 * degree);
      }

      baseTime = switch (degree) {
         case 0 -> 2; // 2 hours
         case 1 -> 24; // 1 day
         case 2 -> 168; // 7 days
         default -> 1680; // 70 days
      };

      return 3600000 * (baseTime + subDegreeTime);
   }
}