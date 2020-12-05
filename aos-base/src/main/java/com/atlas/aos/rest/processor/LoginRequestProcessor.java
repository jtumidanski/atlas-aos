package com.atlas.aos.rest.processor;

import builder.ErrorBodyBuilder;
import builder.ResultBuilder;
import com.atlas.aos.ConfigurationRegistry;
import com.atlas.aos.attribute.LoginAttributes;
import com.atlas.aos.database.provider.IpBanProvider;
import com.atlas.aos.database.provider.MacBanProvider;
import com.atlas.aos.model.AccountData;
import com.atlas.aos.model.LoginState;
import com.atlas.aos.processor.AccountProcessor;
import database.Connection;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.core.Response;
import java.util.*;

public final class LoginRequestProcessor {
   private LoginRequestProcessor() {
   }


   public static ResultBuilder login(LoginAttributes attributes) {
      if (getSessionAttempts(attributes.sessionId()) > 4) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("TOO_MANY_ATTEMPTS"));
      }
      String accountName = attributes.name();
      Optional<AccountData> result = AccountProcessor.getAccountByName(accountName);
      AccountData account;
      if (result.isEmpty()) {
         if (ConfigurationRegistry.getInstance().getConfiguration().automaticRegister) {
            String password = BCrypt.hashpw(attributes.password(), BCrypt.gensalt(12));
            account = AccountProcessor.createAccount(accountName, password);
         } else {
            return new ResultBuilder(Response.Status.FORBIDDEN)
                  .addError(new ErrorBodyBuilder().setCode("NOT_REGISTERED"));
         }
      } else {
         account = result.get();
      }

      if (account.banned()) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
                     .addMeta("reason", (byte) account.gReason())
               );
      }

      if (checkIpBan(attributes.ipAddress())) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
               );
      }

      List<String> macs = account.macs() == null ? Collections.emptyList() : Arrays.asList(account.macs().split(", "));
      if (checkMacBan(macs)) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
               );
      }

      Optional<Calendar> tempBanCalendar = AccountProcessor.getTempBanCalendar(account.id());
      if (tempBanCalendar.isPresent() && tempBanCalendar.get().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
                     .addMeta("reason", (byte) account.gReason())
                     .addMeta("tempBan", tempBanCalendar.get().getTimeInMillis())
               );
      }

      String passwordHash = account.password();
      boolean tos = account.tos();

      if (getLoginState(account.id()) != LoginState.NOT_LOGGED_IN) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("ALREADY_LOGGED_IN"));
      } else if (passwordHash.charAt(0) == '$' && passwordHash.charAt(1) == '2' &&
            BCrypt.checkpw(attributes.password(), passwordHash)) {
         if (tos) {
            return new ResultBuilder(Response.Status.FORBIDDEN)
                  .addError(new ErrorBodyBuilder().setCode("LICENSE_AGREEMENT"));
         }
      } else {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("INCORRECT_PASSWORD"));
      }

      AccountProcessor.updateLoggedInStatus(account.id(), LoginState.LOGGED_IN);

      // set login state
      return new ResultBuilder(Response.Status.NO_CONTENT);
   }


   protected static boolean checkMacBan(List<String> macs) {
      return Connection.instance()
            .element(entityManager -> MacBanProvider.getMacBanCount(entityManager, macs))
            .orElse(0L) > 0;
   }

   protected static boolean checkIpBan(String ipAddress) {
      return Connection.instance()
            .element(entityManager -> IpBanProvider.getIpBanCount(entityManager, ipAddress))
            .orElse(0L) > 0;
   }

   protected static int getSessionAttempts(long sessionId) {
      return 0;
   }

   protected static LoginState getLoginState(int accountId) {
      Optional<AccountData> result = AccountProcessor.getAccountById(accountId);
      if (result.isEmpty()) {
         return LoginState.NOT_LOGGED_IN;
      }

      AccountData account = result.get();
      LoginState state = LoginState.from(account.loggedIn()).orElseThrow();

      if (state == LoginState.SERVER_TRANSITION) {
         if (account.lastLogin().getTime() + 30000 < System.currentTimeMillis()) {
            state = LoginState.NOT_LOGGED_IN;
            AccountProcessor.updateLoggedInStatus(accountId, state);
         }
      }

      if (state == LoginState.SERVER_TRANSITION) {
         AccountProcessor.updateLoggedInStatus(accountId, state);
      }

      return state;
   }

   public static ResultBuilder logout(int accountId) {
      AccountProcessor.updateLoggedInStatus(accountId, LoginState.NOT_LOGGED_IN);
      return new ResultBuilder(Response.Status.NO_CONTENT);
   }
}
