package com.example.livechat.exception;

// 사용자가 참가한 대화방 아님
public class NotContainsUserChatException extends RuntimeException {

    public NotContainsUserChatException(String message) {
        super(message);
    }

    public NotContainsUserChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotContainsUserChatException(Throwable cause) {
        super(cause);
    }

    public NotContainsUserChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
