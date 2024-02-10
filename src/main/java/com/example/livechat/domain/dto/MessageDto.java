package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long messageId;
    private String contents;
    private MemberDto sender;

    public MessageDto(Message message) {
        this.messageId = message.getId();
        this.contents = message.getContents();
        this.sender = new MemberDto(message.getSender());
    }
}
