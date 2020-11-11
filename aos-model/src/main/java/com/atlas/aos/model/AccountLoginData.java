package com.atlas.aos.model;

import java.sql.Timestamp;
import java.util.Date;

public record AccountLoginData(int loggedIn, Timestamp lastLogin, Date birthday) {
}
