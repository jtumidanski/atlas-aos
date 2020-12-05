package com.atlas.aos.processor;

import java.util.Calendar;
import java.util.Optional;

import com.atlas.aos.model.LoginState;
import com.atlas.aos.database.administrator.AccountAdministrator;
import com.atlas.aos.database.provider.AccountProvider;
import com.atlas.aos.model.AccountData;

import database.Connection;

public final class AccountProcessor {
   private AccountProcessor() {
   }

   public static Optional<AccountData> getAccountByName(String name) {
      return Connection.instance()
            .list(entityManager -> AccountProvider.getAccountsByName(entityManager, name))
            .stream()
            .findFirst();
   }

   public static AccountData createAccount(String name, String password) {
      return Connection.instance()
            .element(entityManager -> AccountAdministrator.create(entityManager, name, password))
            .orElseThrow();
   }

   public static Optional<Calendar> getTempBanCalendar(int accountId) {
      return Connection.instance()
            .element(entityManager -> AccountProvider.getTempBanCalendar(entityManager, accountId));
   }

   public static Optional<AccountData> getAccountById(int accountId) {
      return Connection.instance()
            .element(entityManager -> AccountProvider.getAccountDataById(entityManager, accountId));
   }

   public static void updateLoggedInStatus(int accountId, LoginState state) {
      Connection.instance()
            .with(entityManager -> AccountAdministrator.update(entityManager, accountId, null, state.getValue()));
   }
}
