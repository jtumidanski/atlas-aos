package com.atlas.aos.database.administrator;

import javax.persistence.EntityManager;

import com.app.database.util.QueryAdministratorUtil;
import com.atlas.aos.entity.HwidBan;

public class HwidBanAdministrator {
   private HwidBanAdministrator() {
   }

   public static void banHwid(EntityManager entityManager, String hwid) {
      HwidBan hwidBan = new HwidBan();
      hwidBan.setHwid(hwid);
      QueryAdministratorUtil.insert(entityManager, hwidBan);
   }
}
