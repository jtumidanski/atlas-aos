package com.atlas.aos.database.administrator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;

import com.app.database.util.QueryAdministratorUtil;
import com.atlas.aos.database.transformer.AccountDataTransformer;
import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountData;

public class AccountAdministrator {
   private AccountAdministrator() {
   }

   protected static void update(EntityManager entityManager, int id, Consumer<Account> consumer) {
      QueryAdministratorUtil.update(entityManager, Account.class, id, consumer);
   }

   public static void update(EntityManager entityManager, int accountId, String password, Integer loggedIn) {
      update(entityManager, accountId, account -> {
         QueryAdministratorUtil.setIfNotNull(password, account::setPassword);
         QueryAdministratorUtil.setIfNotNull(loggedIn, account::setLoggedIn);
      });
   }

   public static Optional<AccountData> create(EntityManager entityManager, String name, String password) {
      Account account = new Account();
      account.setName(name);
      account.setPassword(password);
      account.setBirthday(new Date(0));
      account.setTempBan(new Timestamp(0));
      QueryAdministratorUtil.insert(entityManager, account);
      return Optional.of(new AccountDataTransformer().transform(account));
   }
}
