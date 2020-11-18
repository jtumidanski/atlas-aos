package com.atlas.aos.builder;

import java.util.Date;

import com.app.common.builder.RecordBuilder;
import com.atlas.aos.attribute.AccountAttributes;

import builder.AttributeResultBuilder;

public class AccountAttributesBuilder extends RecordBuilder<AccountAttributes, AccountAttributesBuilder>
      implements AttributeResultBuilder {
   private String name;

   private String password;

   private String pin;

   private String pic;

   private Integer loggedIn;

   private Date lastLogin;

   private Byte gender;

   private Boolean banned;

   private Boolean tos;

   private String language;

   private String country;

   private Short characterSlots;

   @Override
   public AccountAttributes construct() {
      return new AccountAttributes(name, password, pin, pic, loggedIn, lastLogin, gender, banned, tos, language, country,
            characterSlots);
   }

   @Override
   public AccountAttributesBuilder getThis() {
      return this;
   }

   public AccountAttributesBuilder setName(String name) {
      this.name = name;
      return getThis();
   }

   public AccountAttributesBuilder setPassword(String password) {
      this.password = password;
      return getThis();
   }

   public AccountAttributesBuilder setPin(String pin) {
      this.pin = pin;
      return getThis();
   }

   public AccountAttributesBuilder setPic(String pic) {
      this.pic = pic;
      return getThis();
   }

   public AccountAttributesBuilder setLoggedIn(Integer loggedIn) {
      this.loggedIn = loggedIn;
      return getThis();
   }

   public AccountAttributesBuilder setLastLogin(Date lastLogin) {
      this.lastLogin = lastLogin;
      return getThis();
   }

   public AccountAttributesBuilder setGender(Byte gender) {
      this.gender = gender;
      return getThis();
   }

   public AccountAttributesBuilder setBanned(Boolean banned) {
      this.banned = banned;
      return getThis();
   }

   public AccountAttributesBuilder setTos(Boolean tos) {
      this.tos = tos;
      return getThis();
   }

   public AccountAttributesBuilder setLanguage(String language) {
      this.language = language;
      return getThis();
   }

   public AccountAttributesBuilder setCountry(String country) {
      this.country = country;
      return getThis();
   }

   public AccountAttributesBuilder setCharacterSlots(Short characterSlots) {
      this.characterSlots = characterSlots;
      return getThis();
   }
}
