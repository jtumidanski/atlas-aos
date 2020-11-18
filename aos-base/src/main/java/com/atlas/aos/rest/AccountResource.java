package com.atlas.aos.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.app.rest.util.stream.Mappers;
import com.atlas.aos.processor.AccountProcessor;

import builder.ResultBuilder;

@Path("accounts")
public class AccountResource {
   @GET
   @Path("")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAccount(@QueryParam("name") String name) {
      ResultBuilder resultBuilder = new ResultBuilder(Response.Status.NOT_FOUND);
      if (name != null) {
         resultBuilder = AccountProcessor.getInstance()
               .getAccountByName(name)
               .map(ResultObjectFactory::create)
               .map(Mappers::singleResult)
               .orElse(new ResultBuilder(Response.Status.NOT_FOUND));
      }
      return resultBuilder.build();
   }

   @GET
   @Path("/{accountId}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAccount(@PathParam("accountId") Integer accountId) {
      ResultBuilder resultBuilder = AccountProcessor.getInstance()
            .getAccountById(accountId)
            .map(ResultObjectFactory::create)
            .map(Mappers::singleResult)
            .orElse(new ResultBuilder(Response.Status.NOT_FOUND));
      return resultBuilder.build();
   }
}
