package com.atlas.aos.database.transformer;

import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountData;

import transformer.SqlTransformer;

public class AccountDataTransformer implements SqlTransformer<AccountData, Account> {
   @Override
   public AccountData transform(Account account) {
      return new AccountData(account.getId(),
            account.getName(),
            account.getPassword(),
            account.getGender(),
            account.getBanned(),
            account.getPin(),
            account.getPic(),
            account.getCharacterSlots(),
            account.getTos(),
            account.getLanguage(),
            account.getCountry(),
            account.getGReason(),
            account.getLoggedIn(),
            account.getLastLogin(),
            account.getMacs());
   }
}
