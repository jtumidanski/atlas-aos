package com.atlas.aos.attribute;

import java.util.Date;

import rest.AttributeResult;

public record AccountAttributes(String name, String password, String pin, String pic, Integer loggedIn, Date lastLogin,
                                Byte gender, Boolean banned, Boolean tos, String language, String country,
                                Short characterSlots) implements AttributeResult {
}
