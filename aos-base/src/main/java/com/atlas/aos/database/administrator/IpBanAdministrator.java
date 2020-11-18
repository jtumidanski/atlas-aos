package com.atlas.aos.database.administrator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.app.database.util.QueryAdministratorUtil;
import com.atlas.aos.entity.IpBan;

public class IpBanAdministrator {
   private IpBanAdministrator() {
   }

   public static void banIp(EntityManager entityManager, String ip) {
      IpBan ipBan = new IpBan();
      ipBan.setIp(ip);
      QueryAdministratorUtil.insert(entityManager, ip);
   }

   public static void banIp(EntityManager entityManager, String ip, int accountId) {
      IpBan ipBan = new IpBan();
      ipBan.setIp(ip);
      ipBan.setAid(accountId);
      QueryAdministratorUtil.insert(entityManager, ipBan);
   }

   public static void removeIpBan(EntityManager entityManager, int accountId) {
      Query query = entityManager.createQuery("DELETE FROM IpBan WHERE aid = :accountId");
      query.setParameter("accountId", accountId);
      QueryAdministratorUtil.execute(entityManager, query);
   }
}
