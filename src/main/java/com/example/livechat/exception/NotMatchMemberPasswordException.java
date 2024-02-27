package com.example.livechat.exception;

// 사용자 비밀번호 다름
public class NotMatchMemberPasswordException extends RuntimeException {

    public NotMatchMemberPasswordException(String message) {
        super(message);
    }

    public NotMatchMemberPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchMemberPasswordException(Throwable cause) {
        super(cause);
    }

    public NotMatchMemberPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
