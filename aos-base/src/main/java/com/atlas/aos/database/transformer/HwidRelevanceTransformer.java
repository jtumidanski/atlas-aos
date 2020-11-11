package com.atlas.aos.database.transformer;

import com.atlas.aos.entity.HwidAccount;
import com.atlas.aos.model.HwidAccountData;

import transformer.SqlTransformer;

public class HwidRelevanceTransformer implements SqlTransformer<HwidAccountData, HwidAccount> {
   @Override
   public HwidAccountData transform(HwidAccount hwidAccount) {
      return new HwidAccountData(
            hwidAccount.getId(),
            hwidAccount.getAccountId(),
            hwidAccount.getHwid(),
            hwidAccount.getRelevance(),
            hwidAccount.getExpiresAt());
   }
}
