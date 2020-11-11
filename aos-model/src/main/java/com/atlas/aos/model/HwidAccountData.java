package com.atlas.aos.model;

import java.util.Date;

public record HwidAccountData(int id, int accountId, String hwid, int relevance, Date expiration) {
}
