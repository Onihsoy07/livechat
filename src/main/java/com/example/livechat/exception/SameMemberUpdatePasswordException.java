package com.example.livechat.exception;

public class SameMemberUpdatePasswordException extends RuntimeException {

    public SameMemberUpdatePasswordException(String message) {
        super(message);
    }

    public SameMemberUpdatePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameMemberUpdatePasswordException(Throwable cause) {
        super(cause);
    }

    public SameMemberUpdatePasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
