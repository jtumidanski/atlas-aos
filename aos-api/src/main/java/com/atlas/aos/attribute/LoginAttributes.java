package com.atlas.aos.attribute;

import rest.AttributeResult;

public record LoginAttributes(Long sessionId, String name, String password, String ipAddress,
                              Integer state) implements AttributeResult {
}
