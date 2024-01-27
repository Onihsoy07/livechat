package com.example.livechat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public String illegalArgumentExceptionHandler (IllegalArgumentException e, HttpServletRequest request) {
        log.error("[exceptionHandler] IllegalArgumentException", e);
        return "error/404";
    }

}
