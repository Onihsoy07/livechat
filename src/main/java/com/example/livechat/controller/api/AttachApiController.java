package com.example.livechat.controller.api;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.AttachDownloadDto;
import com.example.livechat.domain.dto.AttachDto;
import com.example.livechat.domain.dto.HttpResponseDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.service.AttachService;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/attachs")
@RequiredArgsConstructor
public class AttachApiController {

    private final AttachService attachService;

    @PostMapping
    public HttpResponseDto<AttachDto> saveAttach(@RequestPart(value = "file") MultipartFile file,
                                                 @RequestPart(value = "message") MessageSaveDto messageSaveDto,
                                                 @CurrentMember Member member) {
        AttachDto attachDto = null;
        try {
            attachDto = attachService.save(file, messageSaveDto, member);
        } catch (IOException e) {
            log.info("파일 저장 에러", e);
            return new HttpResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "", null);
        }
        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "", null);
    }

    @GetMapping("/{attachStoreFileName}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("attachStoreFileName") final String attachStoreFileName,
                                                   HttpServletResponse response) {
        AttachDownloadDto attachDownloadDto = null;

        try {
            attachDownloadDto = attachService.downloadAttach(attachStoreFileName);
            Path fullPath = Paths.get(attachDownloadDto.getStoreFullPath());
            File file = new File(attachDownloadDto.getStoreFullPath());

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, attachDownloadDto.getContentDisposition())
                    .header("Content-Type", Files.probeContentType(fullPath))
                    .body(attachDownloadDto.getUrlResource());
        } catch (MalformedURLException e) {
            log.info("파일 다운로드 MalformedURLException", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        catch (IOException e) {
            log.info("파일 다운로드 IOException", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
