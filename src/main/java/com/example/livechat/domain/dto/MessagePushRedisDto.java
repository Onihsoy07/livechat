package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Message;
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
    private MemberDto sender;
    private MessageGroupDto messageGroupDto;
    private LocalDateTime createDate;

    public MessagePushRedisDto(Message message) {
        this.id = message.getId();
        this.contents = message.getContents();
        this.sender = new MemberDto(message.getSender());
        this.messageGroupDto = new MessageGroupDto(message.getMessageGroup());
        this.createDate = message.getCreateAt();
    }
}
