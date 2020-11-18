package com.atlas.aos.builder;

import builder.AttributeResultBuilder;
import builder.RecordBuilder;
import com.atlas.aos.attribute.LoginAttributes;

public class LoginAttributesBuilder extends RecordBuilder<LoginAttributes, LoginAttributesBuilder> implements AttributeResultBuilder {
   private static final String SESSION_ID = "SESSION_ID";

   private static final String NAME = "NAME";

   private static final String PASSWORD = "PASSWORD";

   private static final String IP_ADDRESS = "IP_ADDRESS";

   private static final String STATE = "STATE";


   @Override
   public LoginAttributes construct() {
      return new LoginAttributes(get(SESSION_ID), get(NAME), get(PASSWORD), get(IP_ADDRESS), get(STATE));
   }

   @Override
   public LoginAttributesBuilder getThis() {
      return this;
   }

   public LoginAttributesBuilder setSessionId(long sessionId) {
      return set(SESSION_ID, sessionId);
   }

   public LoginAttributesBuilder setName(String name) {
      return set(NAME, name);
   }

   public LoginAttributesBuilder setPassword(String password) {
      return set(PASSWORD, password);
   }

   public LoginAttributesBuilder setIpAddress(String ipAddress) {
      return set(IP_ADDRESS, ipAddress);
   }

   public LoginAttributesBuilder setState(int state) {
      return set(STATE, state);
   }

}
