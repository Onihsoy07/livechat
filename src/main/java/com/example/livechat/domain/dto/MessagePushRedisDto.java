package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.enumerate.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePushRedisDto {

    private Long id;
    private String contents;
    private MessageType messageType;
    private MemberDto sender;
    private ChatDto chatDto;
    private AttachDto attachDto;
    private String createDate;

    public MessagePushRedisDto(Message message) {
        this.id = message.getId();
        this.contents = message.getContents();
        this.messageType = message.getMessageType();
        this.sender = new MemberDto(message.getSender());
        this.chatDto = new ChatDto(message.getChat());
        this.createDate = message.getCreateAt().toString();
        if (message.getAttach() != null) {
            this.attachDto = new AttachDto(message.getAttach());
        }
    }
}
