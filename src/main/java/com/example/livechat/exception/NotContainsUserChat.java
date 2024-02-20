package com.example.livechat.exception;

public class NotContainsUserChat extends RuntimeException {

    public NotContainsUserChat(String message) {
        super(message);
    }

    public NotContainsUserChat(String message, Throwable cause) {
        super(message, cause);
    }

    public NotContainsUserChat(Throwable cause) {
        super(cause);
    }

    public NotContainsUserChat(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
