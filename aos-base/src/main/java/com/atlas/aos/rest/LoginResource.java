package com.atlas.aos.rest;

import com.atlas.aos.attribute.LoginAttributes;
import com.atlas.aos.rest.processor.LoginRequestProcessor;
import rest.InputBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("logins")
public class LoginResource {
   @POST
   @Path("")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response loginToAccount(InputBody<LoginAttributes> inputBody) {
      return LoginRequestProcessor.login(inputBody.attributes()).build();
   }

   @DELETE
   @Path("/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response logoutAccount(@PathParam("id") Integer accountId) {
      return LoginRequestProcessor.logout(accountId).build();
   }
}
