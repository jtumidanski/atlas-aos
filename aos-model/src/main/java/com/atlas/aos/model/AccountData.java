package com.atlas.aos.model;

import java.util.Date;

public record AccountData(int id, String name, String password, int gender, boolean banned, String pin, String pic,
                          int characterSlots, boolean tos, String language, String country, int gReason, int loggedIn,
                          Date lastLogin, String macs) {
}
