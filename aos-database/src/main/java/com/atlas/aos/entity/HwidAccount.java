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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NamedQueries({
      @NamedQuery(name = HwidAccount.GET_BY_ACCOUNT_ID, query = "SELECT a FROM HwidAccount a WHERE a.accountId = :accountId"),
      @NamedQuery(name = HwidAccount.GET_BY_ACCOUNT_ID_AND_HWID,
            query = "SELECT a FROM HwidAccount a WHERE a.accountId = :accountId AND a.hwid = :hwid"),
      @NamedQuery(name = HwidAccount.GET_BY_ID, query = "SELECT a FROM HwidAccount a WHERE a.id = :id"),
})
@Table(name = "hwidaccounts", uniqueConstraints = {
      @UniqueConstraint(columnNames = { "accountId", "hwid" })
})
public class HwidAccount implements Serializable {
   private static final long serialVersionUID = 1L;

   public static final String GET_BY_ID = "HwidAccount.GET_BY_ID";

   public static final String GET_BY_ACCOUNT_ID = "HwidAccount.GET_BY_ACCOUNT_ID";

   public static final String GET_BY_ACCOUNT_ID_AND_HWID = "HwidAccount.GET_BY_ACCOUNT_ID_AND_HWID";

   public static final String ACCOUNT_ID = "accountId";

   public static final String HWID = "hwid";

   public static final String ID = "id";

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
