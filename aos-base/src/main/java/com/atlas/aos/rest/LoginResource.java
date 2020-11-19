package com.atlas.aos.rest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlas.aos.LoginState;
import com.atlas.aos.attribute.LoginAttributes;
import com.atlas.aos.database.provider.IpBanProvider;
import com.atlas.aos.database.provider.MacBanProvider;
import com.atlas.aos.model.AccountData;
import com.atlas.aos.processor.AccountProcessor;
import com.atlas.aos.processor.ConfigurationProcessor;
import org.mindrot.jbcrypt.BCrypt;

import builder.ErrorBodyBuilder;
import builder.ResultBuilder;
import database.Connection;
import rest.InputBody;

@Path("logins")
public class LoginResource {

   @POST
   @Path("")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response loginToAccount(InputBody<LoginAttributes> inputBody) {
      if (getSessionAttempts(inputBody.attributes().sessionId()) > 4) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("TOO_MANY_ATTEMPTS")).build();
      }
      String accountName = inputBody.attribute(LoginAttributes::name);
      Optional<AccountData> result = AccountProcessor.getInstance().getAccountByName(accountName);
      AccountData account;
      if (result.isEmpty()) {
         if (ConfigurationProcessor.getInstance().getConfiguration().automaticRegister) {
            String password = BCrypt.hashpw(inputBody.attribute(LoginAttributes::password), BCrypt.gensalt(12));
            account = AccountProcessor.getInstance().createAccount(accountName, password);
         } else {
            return new ResultBuilder(Response.Status.FORBIDDEN)
                  .addError(new ErrorBodyBuilder().setCode("NOT_REGISTERED")).build();
         }
      } else {
         account = result.get();
      }

      if (account.banned()) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
                     .addMeta("reason", (byte) account.gReason())
               ).build();
      }

      if (checkIpBan(inputBody.attribute(LoginAttributes::ipAddress))) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
               ).build();
      }

      List<String> macs = account.macs() == null ? Collections.emptyList() : Arrays.asList(account.macs().split(", "));
      if (checkMacBan(macs)) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
               ).build();
      }

      Optional<Calendar> tempBanCalendar = AccountProcessor.getInstance().getTempBanCalendar(account.id());
      if (tempBanCalendar.isPresent() && tempBanCalendar.get().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder()
                     .setCode("DELETED_OR_BLOCKED")
                     .addMeta("reason", (byte) account.gReason())
                     .addMeta("tempBan", tempBanCalendar.get().getTimeInMillis())
               ).build();
      }

      String passwordHash = account.password();
      boolean tos = account.tos();

      if (getLoginState(account.id()) != LoginState.NOT_LOGGED_IN) {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("ALREADY_LOGGED_IN")).build();
      } else if (passwordHash.charAt(0) == '$' && passwordHash.charAt(1) == '2' &&
            BCrypt.checkpw(inputBody.attribute(LoginAttributes::password), passwordHash)) {
         if (tos) {
            return new ResultBuilder(Response.Status.FORBIDDEN)
                  .addError(new ErrorBodyBuilder().setCode("LICENSE_AGREEMENT")).build();
         }
      } else {
         return new ResultBuilder(Response.Status.FORBIDDEN)
               .addError(new ErrorBodyBuilder().setCode("INCORRECT_PASSWORD")).build();
      }

      AccountProcessor.getInstance().updateLoggedInStatus(account.id(), LoginState.LOGGED_IN);

      // set login state
      return new ResultBuilder(Response.Status.NO_CONTENT).build();
   }

   @DELETE
   @Path("/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response logoutAccount(@PathParam("id") Integer accountId) {
      updateLoginState(accountId, LoginState.NOT_LOGGED_IN);
      return new ResultBuilder(Response.Status.NO_CONTENT).build();
   }

   protected boolean checkMacBan(List<String> macs) {
      return Connection.instance()
            .element(entityManager -> MacBanProvider.getMacBanCount(entityManager, macs))
            .orElse(0L) > 0;
   }

   protected boolean checkIpBan(String ipAddress) {
      return Connection.instance()
            .element(entityManager -> IpBanProvider.getIpBanCount(entityManager, ipAddress))
            .orElse(0L) > 0;
   }

   protected int getSessionAttempts(long sessionId) {
      return 0;
   }

   protected LoginState getLoginState(int accountId) {
      // 0 = LOGIN_NOT_LOGGED_IN, 1= LOGIN_SERVER_TRANSITION, 2 = LOGIN_LOGGED_IN
      Optional<AccountData> result = AccountProcessor.getInstance().getAccountById(accountId);
      if (result.isEmpty()) {
         return LoginState.NOT_LOGGED_IN;
      }

      AccountData account = result.get();
      LoginState state = LoginState.from(account.loggedIn()).orElseThrow();

      if (state == LoginState.SERVER_TRANSITION) {
         if (account.lastLogin().getTime() + 30000 < System.currentTimeMillis()) {
            state = LoginState.NOT_LOGGED_IN;
            updateLoginState(accountId, state);
         }
      }

      if (state == LoginState.SERVER_TRANSITION) {
         AccountProcessor.getInstance().updateLoggedInStatus(accountId, state);
      }

      return state;
   }

   protected void updateLoginState(int accountId, LoginState newState) {
      AccountProcessor.getInstance().updateLoggedInStatus(accountId, newState);
   }
}
