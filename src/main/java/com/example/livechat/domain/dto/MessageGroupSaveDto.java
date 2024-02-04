package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroupSaveDto {

    @NotBlank(message = "방이름을 작성해주세요")
    private String chatName;

    private Boolean isOpenChat;

}
