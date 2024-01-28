package com.example.livechat.domain.constant;

public interface JwtVo {

    // HS256 (대칭키)
    public static final String SECRET = "비밀키";
    // 일주일
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
