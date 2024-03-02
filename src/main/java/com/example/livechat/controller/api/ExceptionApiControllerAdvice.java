package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.HttpResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class ExceptionApiControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public HttpResponseDto<Null> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e, HttpServletResponse response) {
        log.info("[exceptionHandler] MaxUploadSizeExceededException", e);
        return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "파일 크기가 100MB를 초과하였습니다.", null);
    }

}
