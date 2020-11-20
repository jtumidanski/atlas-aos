package com.atlas.aos.processor;

import java.util.Calendar;
import java.util.Optional;

import com.atlas.aos.LoginState;
import com.atlas.aos.attribute.AccountAttributes;
import com.atlas.aos.database.administrator.AccountAdministrator;
import com.atlas.aos.database.provider.AccountProvider;
import com.atlas.aos.model.AccountData;

import database.Connection;

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
      return Connection.instance()
            .list(entityManager -> AccountProvider.getAccountsByName(entityManager, name))
            .stream()
            .findFirst();
   }

   public AccountData createAccount(String name, String password) {
      return Connection.instance()
            .element(entityManager -> AccountAdministrator.create(entityManager, name, password))
            .orElseThrow();
   }

   public Optional<Calendar> getTempBanCalendar(int accountId) {
      return Connection.instance()
            .element(entityManager -> AccountProvider.getTempBanCalendar(entityManager, accountId));
   }

   public Optional<AccountData> getAccountById(int accountId) {
      return Connection.instance()
            .element(entityManager -> AccountProvider.getAccountDataById(entityManager, accountId));
   }

   public void updateLoggedInStatus(int accountId, LoginState state) {
      Connection.instance()
            .with(entityManager -> AccountAdministrator.update(entityManager, accountId, null, state.getValue()));
   }
}
