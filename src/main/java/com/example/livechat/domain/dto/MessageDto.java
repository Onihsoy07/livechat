package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.enumerate.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long messageId;
    private String contents;
    private MessageType messageType;
    private MemberDto sender;
    private AttachDto attachDto;
    private String createDate;
    private int notViewer;

    public MessageDto(Message message) {
        this.messageId = message.getId();
        this.contents = message.getContents();
        this.messageType = message.getMessageType();
        this.sender = new MemberDto(message.getSender());
        this.createDate = message.getCreateAt().format(DateTimeFormatter.ofPattern("yy.MM.ddHH:mm"));
        this.notViewer = message.getViewerMemberSet().size();
        if (message.getAttach() != null) {
            this.attachDto = new AttachDto(message.getAttach());
        } else {
            this.attachDto = new AttachDto(null, null);
        }
    }
}
