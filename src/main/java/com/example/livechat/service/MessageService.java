package com.example.livechat.service;

import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.entity.MessageGroup;
import com.example.livechat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageGroupService messageGroupService;

    public void saveMessage(MessageSaveDto messageSaveDto, Member member) {
        MessageGroup messageGroup = messageGroupService.getMessageGroupEntity(messageSaveDto.getChatId());

        Message message = Message.builder()
                .sender(member)
                .messageGroup(messageGroup)
                .contents(messageSaveDto.getMessage())
                .build();

        messageRepository.save(message);
    }
}