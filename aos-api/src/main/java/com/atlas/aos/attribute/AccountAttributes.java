package com.atlas.aos.attribute;

import java.util.Date;

import rest.AttributeResult;

public class AccountAttributes implements AttributeResult {
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

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPin() {
      return pin;
   }

   public void setPin(String pin) {
      this.pin = pin;
   }

   public String getPic() {
      return pic;
   }

   public void setPic(String pic) {
      this.pic = pic;
   }

   public Integer getLoggedIn() {
      return loggedIn;
   }

   public void setLoggedIn(Integer loggedIn) {
      this.loggedIn = loggedIn;
   }

   public Date getLastLogin() {
      return lastLogin;
   }

   public void setLastLogin(Date lastLogin) {
      this.lastLogin = lastLogin;
   }

   public Byte getGender() {
      return gender;
   }

   public void setGender(Byte gender) {
      this.gender = gender;
   }

   public Boolean getBanned() {
      return banned;
   }

   public void setBanned(Boolean banned) {
      this.banned = banned;
   }

   public Boolean getTos() {
      return tos;
   }

   public void setTos(Boolean tos) {
      this.tos = tos;
   }

   public String getLanguage() {
      return language;
   }

   public void setLanguage(String language) {
      this.language = language;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public Short getCharacterSlots() {
      return characterSlots;
   }

   public void setCharacterSlots(Short characterSlots) {
      this.characterSlots = characterSlots;
   }
}
