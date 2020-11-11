package com.atlas.aos.database.transformer;

import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountLoginData;

import transformer.SqlTransformer;

public class AccountLoginDataTransformer implements SqlTransformer<AccountLoginData, Account> {
   @Override
   public AccountLoginData transform(Account account) {
      return new AccountLoginData(account.getLoggedIn(), account.getLastLogin(), account.getBirthday());
   }
}
