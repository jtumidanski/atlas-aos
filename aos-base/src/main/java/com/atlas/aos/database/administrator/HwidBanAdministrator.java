package com.atlas.aos.database.administrator;


import javax.persistence.EntityManager;

import com.atlas.aos.entity.HwidBan;

import accessor.AbstractQueryExecutor;

public class HwidBanAdministrator extends AbstractQueryExecutor {
   private static HwidBanAdministrator instance;

   public static HwidBanAdministrator getInstance() {
      if (instance == null) {
         instance = new HwidBanAdministrator();
      }
      return instance;
   }

   private HwidBanAdministrator() {
   }

   public void banHwid(EntityManager entityManager, String hwid) {
      HwidBan hwidBan = new HwidBan();
      hwidBan.setHwid(hwid);
      insert(entityManager, hwidBan);
   }
}
