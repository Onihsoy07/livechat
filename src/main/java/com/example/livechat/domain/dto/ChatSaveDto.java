package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSaveDto {

    @NotBlank(message = "방이름을 작성해주세요")
    private String chatName;

    private Boolean isOpenChat;

}
