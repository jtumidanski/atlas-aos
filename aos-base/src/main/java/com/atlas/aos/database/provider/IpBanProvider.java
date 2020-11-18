package com.atlas.aos.database.provider;

import java.util.Optional;
import javax.persistence.EntityManager;

import com.app.database.provider.NamedQueryClient;
import com.atlas.aos.entity.IpBan;

public class IpBanProvider {
   private IpBanProvider() {
   }

   public static Optional<Long> getIpBanCount(EntityManager entityManager, String ipAddress) {
      return new NamedQueryClient<>(entityManager, IpBan.COUNT_FOR_IP, Long.class)
            .setParameter(IpBan.IP_ADDRESS, ipAddress)
            .element(Long::longValue);
   }
}
