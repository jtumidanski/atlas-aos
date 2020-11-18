package com.atlas.aos.builder;

import builder.AttributeResultBuilder;
import builder.RecordBuilder;
import com.atlas.aos.attribute.AccountAttributes;

import java.util.Date;

public class AccountAttributesBuilder extends RecordBuilder<AccountAttributes, AccountAttributesBuilder> implements AttributeResultBuilder {
   private static final String NAME = "NAME";

   private static final String PASSWORD = "PASSWORD";

   private static final String PIN = "PIN";

   private static final String PIC = "PIC";

   private static final String LOGGED_IN = "LOGGED_IN";

   private static final String LAST_LOGIN = "LAST_LOGIN";

   private static final String GENDER = "GENDER";

   private static final String BANNED = "BANNED";

   private static final String TOS = "TOS";

   private static final String LANGUAGE = "LANGUAGE";

   private static final String COUNTRY = "COUNTRY";

   private static final String CHARACTER_SLOTS = "CHARACTER_SLOTS";


   @Override
   public AccountAttributes construct() {
      return new AccountAttributes(get(NAME), get(PASSWORD), get(PIN), get(PIC), get(LOGGED_IN), get(LAST_LOGIN), get(GENDER), get(BANNED), get(TOS), get(LANGUAGE), get(COUNTRY), get(CHARACTER_SLOTS));
   }

   @Override
   public AccountAttributesBuilder getThis() {
      return this;
   }

   public AccountAttributesBuilder setName(String name) {
      return set(NAME, name);
   }

   public AccountAttributesBuilder setPassword(String password) {
      return set(PASSWORD, password);
   }

   public AccountAttributesBuilder setPin(String pin) {
      return set(PIN, pin);
   }

   public AccountAttributesBuilder setPic(String pic) {
      return set(PIC, pic);
   }

   public AccountAttributesBuilder setLoggedIn(int loggedIn) {
      return set(LOGGED_IN, loggedIn);
   }

   public AccountAttributesBuilder setLastLogin(Date lastLogin) {
      return set(LAST_LOGIN, lastLogin);
   }

   public AccountAttributesBuilder setGender(byte gender) {
      return set(GENDER, gender);
   }

   public AccountAttributesBuilder setBanned(boolean banned) {
      return set(BANNED, banned);
   }

   public AccountAttributesBuilder setTos(boolean tos) {
      return set(TOS, tos);
   }

   public AccountAttributesBuilder setLanguage(String language) {
      return set(LANGUAGE, language);
   }

   public AccountAttributesBuilder setCountry(String country) {
      return set(COUNTRY, country);
   }

   public AccountAttributesBuilder setCharacterSlots(short characterSlots) {
      return set(CHARACTER_SLOTS, characterSlots);
   }

}
