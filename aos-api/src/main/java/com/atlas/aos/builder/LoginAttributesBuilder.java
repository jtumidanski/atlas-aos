package com.atlas.aos.builder;

import com.atlas.aos.attribute.LoginAttributes;

import builder.AttributeResultBuilder;
import builder.Builder;

public class LoginAttributesBuilder extends Builder<LoginAttributes, LoginAttributesBuilder> implements AttributeResultBuilder {
   @Override
   public LoginAttributes construct() {
      return new LoginAttributes();
   }

   @Override
   public LoginAttributesBuilder getThis() {
      return this;
   }

   public LoginAttributesBuilder setSessionId(Long sessionId) {
      return add(attr -> attr.setSessionId(sessionId));
   }

   public LoginAttributesBuilder setName(String name) {
      return add(attr -> attr.setName(name));
   }

   public LoginAttributesBuilder setPassword(String password) {
      return add(attr -> attr.setPassword(password));
   }

   public LoginAttributesBuilder setIpAddress(String ipAddress) {
      return add(attr -> attr.setIpAddress(ipAddress));
   }

   public LoginAttributesBuilder setState(Integer state) {
      return add(attr -> attr.setState(state));
   }
}
