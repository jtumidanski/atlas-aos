package com.atlas.aos.attribute;

import rest.AttributeResult;

public record LoginAttributes(long sessionId, String name, String password, String ipAddress,
                              int state) implements AttributeResult {
}
