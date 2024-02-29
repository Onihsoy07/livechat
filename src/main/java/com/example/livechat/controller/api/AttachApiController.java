package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.AttachDto;
import com.example.livechat.domain.dto.HttpResponseDto;
import com.example.livechat.service.AttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/attachs")
@RequiredArgsConstructor
public class AttachApiController {

    private final AttachService attachService;

    @PostMapping
    public HttpResponseDto<AttachDto> saveAttach(@RequestPart(value = "file") MultipartFile file) {
        AttachDto attachDto = null;
        try {
            attachDto = attachService.save(file);
        } catch (IOException e) {
            log.info("파일 저장 에러", e);
            return new HttpResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "", null);
        }
        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "", null);
    }


}
