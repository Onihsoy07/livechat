package com.example.livechat.service;

import com.example.livechat.domain.dto.AttachDownloadDto;
import com.example.livechat.domain.dto.AttachDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Attach;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AttachService {

    @Value("${file.dir}")
    private String fileDir;

    private final AttachRepository attachRepository;
    private final ChatService chatService;

    public AttachDto save(MultipartFile multipartFile, MessageSaveDto messageSaveDto) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        Chat chat = chatService.getChatEntity(messageSaveDto.getChatId());
        String originalFilename = multipartFile.getOriginalFilename();
        //서버에 저장하는 파일명 UUID로
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        Attach attach = Attach.builder()
                .uploadFileName(originalFilename)
                .storeFileName(storeFileName)
                .build();

        attachRepository.save(attach);

        return new AttachDto(attach);
    }

    @Transactional(readOnly = true)
    public AttachDownloadDto downloadAttach(String storeFileName) throws MalformedURLException {
        Attach attach = getAttachEntityFindStoreFileName(storeFileName);
        String fullPath = getFullPath(storeFileName);
        UrlResource urlResource = new UrlResource("file:" + fullPath);
        String encodeFileName = UriUtils.encode(attach.getUploadFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeFileName + "\"";

        return new AttachDownloadDto(attach, encodeFileName, fullPath, urlResource, contentDisposition);
    }



    @Transactional(readOnly = true)
    private Attach getAttachEntityFindStoreFileName(String storeFileName) {
        return attachRepository.findByStoreFileName(storeFileName).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("Chat ID : %s 로 찾을 수 없습니다.", storeFileName));
        });
    }

    private String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractedExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
