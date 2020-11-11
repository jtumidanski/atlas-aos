package com.atlas.aos.database.administrator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.function.Consumer;
import javax.persistence.EntityManager;

import com.atlas.aos.database.transformer.AccountDataTransformer;
import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountData;

import accessor.AbstractQueryExecutor;

public class AccountAdministrator extends AbstractQueryExecutor {
   private static AccountAdministrator instance;

   public static AccountAdministrator getInstance() {
      if (instance == null) {
         instance = new AccountAdministrator();
      }
      return instance;
   }

   private AccountAdministrator() {
   }

   protected void update(EntityManager entityManager, int id, Consumer<Account> consumer) {
      super.update(entityManager, Account.class, id, consumer);
   }

   public void update(EntityManager entityManager, int accountId, String password, Integer loggedIn) {
      update(entityManager, accountId, account -> {
         setIfNotNull(password, account::setPassword);
         setIfNotNull(loggedIn, account::setLoggedIn);
      });
   }

   public AccountData create(EntityManager entityManager, String name, String password) {
      Account account = new Account();
      account.setName(name);
      account.setPassword(password);
      account.setBirthday(new Date(0));
      account.setTempBan(new Timestamp(0));
      insert(entityManager, account);
      return new AccountDataTransformer().transform(account);
   }
}
