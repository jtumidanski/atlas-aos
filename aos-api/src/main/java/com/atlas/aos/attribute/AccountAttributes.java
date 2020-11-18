package com.atlas.aos.attribute;

import rest.AttributeResult;

import java.util.Date;

public record AccountAttributes(String name, String password, String pin, String pic, int loggedIn, Date lastLogin,
                                byte gender, boolean banned, boolean tos, String language, String country,
                                short characterSlots) implements AttributeResult {
}
