package com.atlas.aos.database.provider;

import java.util.Optional;
import javax.persistence.EntityManager;

import com.app.database.provider.NamedQueryClient;
import com.atlas.aos.entity.HwidBan;

public class HwidBanProvider {
   private HwidBanProvider() {
   }

   public static Optional<Long> getHwidBanCount(EntityManager entityManager, String hwid) {
      return new NamedQueryClient<>(entityManager, HwidBan.GET_COUNT_BY_HWID, Long.class)
            .setParameter(HwidBan.HWID, hwid)
            .element(Long::longValue);
   }
}
