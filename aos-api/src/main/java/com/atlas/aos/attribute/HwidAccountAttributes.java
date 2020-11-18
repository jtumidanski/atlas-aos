package com.atlas.aos.attribute;

import rest.AttributeResult;

public record HwidAccountAttributes(int accountId, String hwid, int relevance) implements AttributeResult {
}
