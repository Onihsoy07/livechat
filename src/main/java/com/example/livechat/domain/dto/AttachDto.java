package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Attach;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachDto {

    private String uploadFileName;

    private String storeFileName;

    public AttachDto(Attach attach) {
        this.uploadFileName = attach.getUploadFileName();
        this.storeFileName = attach.getStoreFileName();
    }
}
