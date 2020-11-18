package com.atlas.aos.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
      @NamedQuery(name = IpBan.COUNT_FOR_IP, query = "SELECT COUNT(i) FROM IpBan i WHERE :ipAddress LIKE CONCAT(i.ip, '%')")
})
@Table(name = "ipbans")
public class IpBan implements Serializable {
   private static final long serialVersionUID = 1L;

   public static final String COUNT_FOR_IP = "IpBan.COUNT_FOR_IP";

   public static final String IP_ADDRESS = "ipAddress";

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   private Integer ipBanId;

   @Column(nullable = false)
   private String ip;

   @Column(nullable = false)
   private Integer aid;

   public IpBan() {
   }

   public Integer getIpBanId() {
      return ipBanId;
   }

   public void setIpBanId(Integer ipBanId) {
      this.ipBanId = ipBanId;
   }

   public String getIp() {
      return ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public Integer getAid() {
      return aid;
   }

   public void setAid(Integer aid) {
      this.aid = aid;
   }
}
