package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private Long id;
    private String chatName;

    public ChatDto(Chat chat) {
        this.id = chat.getId();
        this.chatName = chat.getChatName();
    }
}
