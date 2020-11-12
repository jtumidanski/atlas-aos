package com.atlas.aos.builder;

import java.util.Date;

import com.atlas.aos.attribute.AccountAttributes;

import builder.AttributeResultBuilder;
import builder.Builder;

public class AccountAttributesBuilder extends Builder<AccountAttributes, AccountAttributesBuilder>
      implements AttributeResultBuilder {
   @Override
   public AccountAttributes construct() {
      return new AccountAttributes();
   }

   @Override
   public AccountAttributesBuilder getThis() {
      return this;
   }

   public AccountAttributesBuilder setName(String name) {
      return add(attr -> attr.setName(name));
   }

   public AccountAttributesBuilder setPassword(String password) {
      return add(attr -> attr.setPassword(password));
   }

   public AccountAttributesBuilder setPin(String pin) {
      return add(attr -> attr.setPin(pin));
   }

   public AccountAttributesBuilder setPic(String pic) {
      return add(attr -> attr.setPic(pic));
   }

   public AccountAttributesBuilder setLoggedIn(Integer loggedIn) {
      return add(attr -> attr.setLoggedIn(loggedIn));
   }

   public AccountAttributesBuilder setLastLogin(Date lastLogin) {
      return add(attr -> attr.setLastLogin(lastLogin));
   }

   public AccountAttributesBuilder setGender(Byte gender) {
      return add(attr -> attr.setGender(gender));
   }

   public AccountAttributesBuilder setBanned(Boolean banned) {
      return add(attr -> attr.setBanned(banned));
   }

   public AccountAttributesBuilder setTos(Boolean tos) {
      return add(attr -> attr.setTos(tos));
   }

   public AccountAttributesBuilder setLanguage(String language) {
      return add(attr -> attr.setLanguage(language));
   }

   public AccountAttributesBuilder setCountry(String country) {
      return add(attr -> attr.setCountry(country));
   }

   public AccountAttributesBuilder setCharacterSlots(Short characterSlots) {
      return add(attr -> attr.setCharacterSlots(characterSlots));
   }
}
