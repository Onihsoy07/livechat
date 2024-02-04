package com.example.livechat.domain.constant;

public interface JwtConst {

    // HS256 (대칭키)
    public static final String SECRET = "E7P4Sc74oXsklSf+UZL1LiKLHkGfYc9atAjFm32hb1H7SiFToLh/Pc5Rfe4cHT7IjOkVlZmU58Er8a9v7CIdw9vR5RV8fXRq";
    // 일주일
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authentication";

}
