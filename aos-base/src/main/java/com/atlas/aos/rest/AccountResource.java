package com.atlas.aos.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlas.aos.attribute.AccountAttributes;
import com.atlas.aos.builder.AccountAttributesBuilder;
import com.atlas.aos.processor.AccountProcessor;

import builder.ResultBuilder;
import builder.ResultObjectBuilder;

@Path("accounts")
public class AccountResource {

   @GET
   @Path("/{accountId}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAccount(@PathParam("accountId") Integer accountId) {
      ResultBuilder resultBuilder = new ResultBuilder(Response.Status.NOT_FOUND);
      AccountProcessor.getInstance().getAccountById(accountId).ifPresent(accountData -> {
         resultBuilder.setStatus(Response.Status.OK);
         resultBuilder.addData(new ResultObjectBuilder(AccountAttributes.class, accountId)
               .setAttribute(new AccountAttributesBuilder()
                     .setName(accountData.name())
                     .setLoggedIn(accountData.loggedIn())
               )
         );
      });
      return resultBuilder.build();
   }
}
