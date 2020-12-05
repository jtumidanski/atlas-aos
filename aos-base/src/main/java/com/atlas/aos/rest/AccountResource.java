package com.atlas.aos.rest;

import com.atlas.aos.rest.processor.AccountRequestProcessor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("accounts")
public class AccountResource {
   @GET
   @Path("")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAccount(@QueryParam("name") String name) {
      return AccountRequestProcessor.getAccounts(name).build();
   }

   @GET
   @Path("/{accountId}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAccount(@PathParam("accountId") Integer accountId) {
      return AccountRequestProcessor.getAccount(accountId).build();
   }
}
