package com.example.livechat.service;

import com.example.livechat.domain.dto.AttachDto;
import com.example.livechat.domain.entity.Attach;
import com.example.livechat.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AttachService {

    @Value("${file.dir}")
    private String fileDir;

    private final AttachRepository attachRepository;

    public AttachDto save(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

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
