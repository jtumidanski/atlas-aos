package com.atlas.aos.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
      @NamedQuery(name = Account.GET_ALL, query = "SELECT a FROM Account a"),
      @NamedQuery(name = Account.GET_BY_NAME, query = "SELECT a FROM Account a WHERE a.name = :name"),
      @NamedQuery(name = Account.GET_BY_ID, query = "SELECT a FROM Account a WHERE a.id = :id"),
      @NamedQuery(name = Account.GET_TEMP_BAN, query = "SELECT a.tempBan FROM Account a WHERE a.id = :id")
})
@Table(name = "accounts", indexes = {
      @Index(name = "name", columnList = "name", unique = true),
      @Index(name = "ranking", columnList = "id,banned"),
      @Index(name = "id_name", columnList = "id,name"),
      @Index(name = "id_nxCredit_maplePoint_nxPrepaid", columnList = "id,nxCredit,maplePoint,nxPrepaid")
})
public class Account implements Serializable {
   private static final long serialVersionUID = 1L;

   public static final String GET_ALL = "Account.GET_ALL";

   public static final String GET_BY_NAME = "Account.GET_BY_NAME";

   public static final String GET_BY_ID = "Account.GET_BY_ID";

   public static final String GET_TEMP_BAN = "Account.GET_TEMP_BAN";

   public static final String NAME = "name";

   public static final String ID = "id";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(nullable = false, length = 13)
   private String name;

   @Column(nullable = false, length = 128)
   private String password;

   @Column(nullable = false, length = 10)
   private String pin = "";

   @Column(nullable = false, length = 26)
   private String pic = "";

   @Column(nullable = false)
   private Integer loggedIn = 0;

   @Column
   private Timestamp lastLogin;

   @Column(nullable = false)
   private Timestamp createDate;

   @Column(nullable = false)
   private Date birthday;

   @Column(nullable = false)
   private Boolean banned = false;

   @Column
   private String banReason;

   @Column
   private String macs;

   @Column(nullable = false)
   private Integer nxCredit = 0;

   @Column(nullable = false)
   private Integer maplePoint = 0;

   @Column(nullable = false)
   private Integer nxPrepaid = 0;

   @Column(nullable = false)
   private Short characterSlots = 3;

   @Column(nullable = false)
   private Integer gender = 1;

   @Column(nullable = false)
   private Timestamp tempBan;

   @Column(nullable = false)
   private Integer gReason = 0;

   @Column(nullable = false)
   private Boolean tos = false;

   private String siteLogged;

   @Column(nullable = false)
   private Integer webAdmin = 0;

   private String nick;

   @Column(nullable = false)
   private Integer mute = 0;

   @Column
   private String email;

   @Column
   private String ip;

   @Column(nullable = false)
   private Integer rewardPoints = 0;

   @Column(nullable = false)
   private Integer votePoints = 0;

   private String hwid;

   @Column(nullable = false)
   private String language = "EN";

   @Column(nullable = false)
   private String country = "US";

   public Account() {
      createDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
      birthday = new Date(0);
      tempBan = new Timestamp(0);
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

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

   public Timestamp getLastLogin() {
      return lastLogin;
   }

   public void setLastLogin(Timestamp lastLogin) {
      this.lastLogin = lastLogin;
   }

   public Timestamp getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Timestamp createDate) {
      this.createDate = createDate;
   }

   public Date getBirthday() {
      return birthday;
   }

   public void setBirthday(Date birthday) {
      this.birthday = birthday;
   }

   public Boolean getBanned() {
      return banned;
   }

   public void setBanned(Boolean banned) {
      this.banned = banned;
   }

   public String getBanReason() {
      return banReason;
   }

   public void setBanReason(String banReason) {
      this.banReason = banReason;
   }

   public String getMacs() {
      return macs;
   }

   public void setMacs(String macs) {
      this.macs = macs;
   }

   public Integer getNxCredit() {
      return nxCredit;
   }

   public void setNxCredit(Integer nxCredit) {
      this.nxCredit = nxCredit;
   }

   public Integer getMaplePoint() {
      return maplePoint;
   }

   public void setMaplePoint(Integer maplePoint) {
      this.maplePoint = maplePoint;
   }

   public Integer getNxPrepaid() {
      return nxPrepaid;
   }

   public void setNxPrepaid(Integer nxPrepaid) {
      this.nxPrepaid = nxPrepaid;
   }

   public Short getCharacterSlots() {
      return characterSlots;
   }

   public void setCharacterSlots(Short characterSlots) {
      this.characterSlots = characterSlots;
   }

   public Integer getGender() {
      return gender;
   }

   public void setGender(Integer gender) {
      this.gender = gender;
   }

   public Timestamp getTempBan() {
      return tempBan;
   }

   public void setTempBan(Timestamp tempBan) {
      this.tempBan = tempBan;
   }

   public Integer getGReason() {
      return gReason;
   }

   public void setGReason(Integer gReason) {
      this.gReason = gReason;
   }

   public Boolean getTos() {
      return tos;
   }

   public void setTos(Boolean tos) {
      this.tos = tos;
   }

   public String getSiteLogged() {
      return siteLogged;
   }

   public void setSiteLogged(String siteLogged) {
      this.siteLogged = siteLogged;
   }

   public Integer getWebAdmin() {
      return webAdmin;
   }

   public void setWebAdmin(Integer webAdmin) {
      this.webAdmin = webAdmin;
   }

   public String getNick() {
      return nick;
   }

   public void setNick(String nick) {
      this.nick = nick;
   }

   public Integer getMute() {
      return mute;
   }

   public void setMute(Integer mute) {
      this.mute = mute;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getIp() {
      return ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public Integer getRewardPoints() {
      return rewardPoints;
   }

   public void setRewardPoints(Integer rewardPoints) {
      this.rewardPoints = rewardPoints;
   }

   public Integer getVotePoints() {
      return votePoints;
   }

   public void setVotePoints(Integer votePoints) {
      this.votePoints = votePoints;
   }

   public String getHwid() {
      return hwid;
   }

   public void setHwid(String hwid) {
      this.hwid = hwid;
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
}
