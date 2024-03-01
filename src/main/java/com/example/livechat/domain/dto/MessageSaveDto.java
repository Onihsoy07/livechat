package com.example.livechat.domain.dto;

import com.example.livechat.domain.enumerate.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSaveDto {

    @NotNull(message = "대화방이 존재하지 않습니다.")
    private Long chatId;

    @NotBlank(message = "대화 내용이 존재하지 않습니다.")
    private String message;

    @NotBlank(message = "메시지 타입이 존재하지 않습니다.")
    private MessageType messageType;

    private String fileName;

}
