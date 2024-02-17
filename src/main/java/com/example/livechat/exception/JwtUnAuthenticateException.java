package com.example.livechat.exception;

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
