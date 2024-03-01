package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Attach;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachDownloadDto {

    private String encodeFileName;

    private String storeFileName;

    private String storeFullPath;

    private UrlResource urlResource;

    private String contentDisposition;

    public AttachDownloadDto(Attach attach, String encodeFileName, String storeFullPath, UrlResource urlResource, String contentDisposition) {
        this.encodeFileName = encodeFileName;
        this.storeFileName = attach.getStoreFileName();
        this.storeFullPath = storeFullPath;
        this.urlResource = urlResource;
        this.contentDisposition = contentDisposition;
    }
}
