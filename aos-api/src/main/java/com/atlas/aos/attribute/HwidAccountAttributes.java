package com.atlas.aos.attribute;

import rest.AttributeResult;

public record HwidAccountAttributes(Integer accountId, String hwid, Integer relevance) implements AttributeResult {
}
