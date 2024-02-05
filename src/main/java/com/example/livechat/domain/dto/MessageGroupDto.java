package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.MessageGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroupDto {

    private String chatName;

    public MessageGroupDto(MessageGroup messageGroup) {
        this.chatName = messageGroup.getMessageGroupName();
    }
}
