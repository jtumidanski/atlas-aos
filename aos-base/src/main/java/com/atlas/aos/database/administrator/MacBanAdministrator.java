package com.atlas.aos.database.administrator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.app.database.util.QueryAdministratorUtil;
import com.atlas.aos.entity.MacBan;

public class MacBanAdministrator {
   private MacBanAdministrator() {
   }

   public static void addMacBan(EntityManager entityManager, int accountId, Set<String> macs, List<String> filtered) {
      List<MacBan> macBanList = macs.stream().filter(mac -> !filtered.contains(mac)).map(mac -> {
         MacBan macBan = new MacBan();
         macBan.setMac(mac);
         macBan.setAid(String.valueOf(accountId));
         return macBan;
      }).collect(Collectors.toList());
      QueryAdministratorUtil.insertBulk(entityManager, macBanList);
   }

   public static void removeMacBan(EntityManager entityManager, int accountId) {
      Query query = entityManager.createQuery("DELETE FROM MacBan WHERE aid = :accountId");
      query.setParameter("accountId", accountId);
      QueryAdministratorUtil.execute(entityManager, query);
   }
}
