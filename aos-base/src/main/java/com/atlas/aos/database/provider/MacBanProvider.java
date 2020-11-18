package com.atlas.aos.database.provider;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.app.database.provider.NamedQueryClient;
import com.atlas.aos.entity.MacBan;

public class MacBanProvider {
   private MacBanProvider() {
   }

   public static Optional<Long> getMacBanCount(EntityManager entityManager, List<String> macs) {
      return new NamedQueryClient<>(entityManager, MacBan.COUNT_BY_MAC_LIST, Long.class)
            .setParameter(MacBan.MACS, macs)
            .element(Long::longValue);
   }
}
