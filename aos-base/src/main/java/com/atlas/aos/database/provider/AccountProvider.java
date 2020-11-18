package com.atlas.aos.database.provider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.app.database.provider.NamedQueryClient;
import com.atlas.aos.database.transformer.AccountDataTransformer;
import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountData;

public class AccountProvider {
   private AccountProvider() {
   }

   public static List<AccountData> getAccounts(EntityManager entityManager) {
      return new NamedQueryClient<>(entityManager, Account.GET_ALL, Account.class)
            .list(new AccountDataTransformer());
   }

   public static List<AccountData> getAccountsByName(EntityManager entityManager, String name) {
      return new NamedQueryClient<>(entityManager, Account.GET_BY_NAME, Account.class)
            .setParameter(Account.NAME, name)
            .list(new AccountDataTransformer());
   }

   public static Optional<AccountData> getAccountDataById(EntityManager entityManager, int accountId) {
      return new NamedQueryClient<>(entityManager, Account.GET_BY_ID, Account.class)
            .setParameter(Account.ID, accountId)
            .element(new AccountDataTransformer());
   }

   public static Optional<Calendar> getTempBanCalendar(EntityManager entityManager, int accountId) {
      return new NamedQueryClient<>(entityManager, Account.GET_TEMP_BAN, Date.class)
            .setParameter(Account.ID, accountId)
            .element(date -> {
               Calendar tempBan = Calendar.getInstance();
               tempBan.setTimeInMillis(date.getTime());
               return tempBan;
            });
   }
}
