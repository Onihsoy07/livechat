package com.example.livechat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpResponseDto<T> {

    private Integer httpStatusCode;
    private Boolean success;
    private String message;
    private T data;

}
