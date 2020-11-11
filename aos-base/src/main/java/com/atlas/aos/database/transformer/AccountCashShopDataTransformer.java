package com.atlas.aos.database.transformer;

import com.atlas.aos.entity.Account;
import com.atlas.aos.model.AccountCashShopData;

import transformer.SqlTransformer;

public class AccountCashShopDataTransformer implements SqlTransformer<AccountCashShopData, Account> {
   @Override
   public AccountCashShopData transform(Account account) {
      return new AccountCashShopData(account.getNxCredit(), account.getMaplePoint(), account.getNxPrepaid());
   }
}
