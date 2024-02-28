package com.example.livechat.exception;

public class NotMatchMemberPasswordCheckException extends RuntimeException {

    public NotMatchMemberPasswordCheckException(String message) {
        super(message);
    }

    public NotMatchMemberPasswordCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchMemberPasswordCheckException(Throwable cause) {
        super(cause);
    }

    public NotMatchMemberPasswordCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
