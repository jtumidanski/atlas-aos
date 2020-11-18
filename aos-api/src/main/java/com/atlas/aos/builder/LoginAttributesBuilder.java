package com.atlas.aos.builder;

import com.app.common.builder.RecordBuilder;
import com.atlas.aos.attribute.LoginAttributes;

import builder.AttributeResultBuilder;

public class LoginAttributesBuilder extends RecordBuilder<LoginAttributes, LoginAttributesBuilder>
      implements AttributeResultBuilder {
   private long sessionId;

   private String name;

   private String password;

   private String ipAddress;

   private int state;

   @Override
   public LoginAttributes construct() {
      return new LoginAttributes(sessionId, name, password, ipAddress, state);
   }

   @Override
   public LoginAttributesBuilder getThis() {
      return this;
   }

   public LoginAttributesBuilder setSessionId(long sessionId) {
      this.sessionId = sessionId;
      return getThis();
   }

   public LoginAttributesBuilder setName(String name) {
      this.name = name;
      return getThis();
   }

   public LoginAttributesBuilder setPassword(String password) {
      this.password = password;
      return getThis();
   }

   public LoginAttributesBuilder setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
      return getThis();
   }

   public LoginAttributesBuilder setState(int state) {
      this.state = state;
      return getThis();
   }
}
