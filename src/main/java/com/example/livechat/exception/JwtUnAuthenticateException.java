package com.example.livechat.exception;

// JWT 토큰 사용 불가(기한 지남, 토큰 없음)
public class JwtUnAuthenticateException extends RuntimeException {

    public JwtUnAuthenticateException(String message) {
        super(message);
    }

    public JwtUnAuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtUnAuthenticateException(Throwable cause) {
        super(cause);
    }

    public JwtUnAuthenticateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
