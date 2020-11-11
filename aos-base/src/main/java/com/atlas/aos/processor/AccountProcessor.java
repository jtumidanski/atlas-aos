package com.atlas.aos.processor;

import java.util.Calendar;
import java.util.Optional;

import com.atlas.aos.LoginState;
import com.atlas.aos.database.administrator.AccountAdministrator;
import com.atlas.aos.database.provider.AccountProvider;
import com.atlas.aos.model.AccountData;

import database.DatabaseConnection;

public class AccountProcessor {
   private static final Object lock = new Object();

   private static volatile AccountProcessor instance;

   public static AccountProcessor getInstance() {
      AccountProcessor result = instance;
      if (result == null) {
         synchronized (lock) {
            result = instance;
            if (result == null) {
               result = new AccountProcessor();
               instance = result;
            }
         }
      }
      return result;
   }

   public Optional<AccountData> getAccountByName(String name) {
      return DatabaseConnection.getInstance()
            .withConnectionResult(entityManager -> AccountProvider.getInstance()
                  .getAccountsByName(entityManager, name)
                  .stream().findFirst()
                  .orElse(null));
   }

   public AccountData createAccount(String name, String password) {
      return DatabaseConnection.getInstance()
            .withConnectionResult(entityManager -> AccountAdministrator.getInstance().create(entityManager, name, password))
            .orElseThrow();
   }

   public Optional<Calendar> getTempBanCalendar(int accountId) {
      return DatabaseConnection.getInstance()
            .withConnectionResult(entityManager -> AccountProvider.getInstance().getTempBanCalendar(entityManager, accountId));
   }

   public Optional<AccountData> getAccountById(int accountId) {
      return DatabaseConnection.getInstance()
            .withConnectionResult(entityManager -> AccountProvider.getInstance()
                  .getAccountDataById(entityManager, accountId)
                  .stream().findFirst()
                  .orElse(null));
   }

   public void updateLoggedInStatus(int accountId, LoginState state) {
      DatabaseConnection.getInstance().withConnection(entityManager -> AccountAdministrator.getInstance().update(entityManager,
            accountId, null, state.getValue()));
   }
}
