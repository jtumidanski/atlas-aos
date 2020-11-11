package com.atlas.aos.database.provider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.atlas.aos.database.transformer.AccountDataTransformer;
import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountData;

import accessor.AbstractQueryExecutor;

public class AccountProvider extends AbstractQueryExecutor {
   private static AccountProvider instance;

   public static AccountProvider getInstance() {
      if (instance == null) {
         instance = new AccountProvider();
      }
      return instance;
   }

   private AccountProvider() {
   }

   public List<AccountData> getAccounts(EntityManager entityManager) {
      TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a", Account.class);
      return getResultList(query, new AccountDataTransformer());
   }

   public List<AccountData> getAccountsByName(EntityManager entityManager, String name) {
      TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a WHERE a.name = :name", Account.class);
      query.setParameter("name", name);
      return getResultList(query, new AccountDataTransformer());
   }

   public Optional<AccountData> getAccountDataById(EntityManager entityManager, int accountId) {
      TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
      query.setParameter("id", accountId);
      return getSingleOptional(query, new AccountDataTransformer());
   }

   public Calendar getTempBanCalendar(EntityManager entityManager, int accountId) {
      TypedQuery<Date> query = entityManager.createQuery("SELECT a.tempBan FROM Account a WHERE a.id = :id", Date.class);
      query.setParameter("id", accountId);
      Calendar tempBan = Calendar.getInstance();
      tempBan.setTimeInMillis(query.getSingleResult().getTime());
      return tempBan;
   }
}
