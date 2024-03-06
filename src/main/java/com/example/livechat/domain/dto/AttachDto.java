package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Attach;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachDto implements Serializable {

    private String uploadFileName;

    private String storeFileName;

    public AttachDto(Attach attach) {
        this.uploadFileName = attach.getUploadFileName();
        this.storeFileName = attach.getStoreFileName();
    }
}
