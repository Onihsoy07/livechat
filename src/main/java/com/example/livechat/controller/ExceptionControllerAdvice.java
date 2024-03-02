package com.example.livechat.controller;

import com.example.livechat.domain.dto.HttpResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public String illegalArgumentExceptionHandler(IllegalArgumentException e, HttpServletRequest request) {
        log.error("[exceptionHandler] IllegalArgumentException", e);
        return "error/404";
    }

}
