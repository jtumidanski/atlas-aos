package com.atlas.aos.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "hwidaccounts", uniqueConstraints = {
      @UniqueConstraint(columnNames = { "accountId", "hwid" })
})
public class HwidAccount implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(nullable = false)
   private Integer accountId;

   @Column(nullable = false)
   private String hwid;

   @Column(nullable = false)
   private Integer relevance;

   @Column(nullable = false)
   private Timestamp expiresAt;

   public HwidAccount() {
      expiresAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
   }

   public Integer getId() {
      return id;
   }

   public Integer getAccountId() {
      return accountId;
   }

   public void setAccountId(Integer accountId) {
      this.accountId = accountId;
   }

   public String getHwid() {
      return hwid;
   }

   public void setHwid(String hwid) {
      this.hwid = hwid;
   }

   public Integer getRelevance() {
      return relevance;
   }

   public void setRelevance(Integer relevance) {
      this.relevance = relevance;
   }

   public Timestamp getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(Timestamp expiresAt) {
      this.expiresAt = expiresAt;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      HwidAccount that = (HwidAccount) o;
      return accountId.equals(that.accountId) &&
            hwid.equals(that.hwid);
   }

   @Override
   public int hashCode() {
      return Objects.hash(accountId, hwid);
   }
}
