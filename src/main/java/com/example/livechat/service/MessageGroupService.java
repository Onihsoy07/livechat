package com.example.livechat.service;

import com.example.livechat.domain.dto.MessageGroupSaveDto;
import com.example.livechat.domain.entity.MessageGroup;
import com.example.livechat.repository.MessageGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageGroupService {

    private final MessageGroupRepository messageGroupRepository;

    public MessageGroup createChat(MessageGroupSaveDto messageGroupSaveDto) {
        MessageGroup messageGroup = MessageGroup.builder()
                .messageGroupName(messageGroupSaveDto.getChatName())
                .isOpenChat(messageGroupSaveDto.getIsOpenChat())
                .build();

        return messageGroupRepository.save(messageGroup);
    }

}
