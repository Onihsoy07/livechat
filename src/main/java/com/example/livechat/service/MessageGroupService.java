package com.example.livechat.service;

import com.example.livechat.domain.dto.MessageGroupDto;
import com.example.livechat.domain.dto.MessageGroupSaveDto;
import com.example.livechat.domain.entity.MessageGroup;
import com.example.livechat.repository.MessageGroupRepository;
import com.example.livechat.service.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageGroupService {

    private final MessageGroupRepository messageGroupRepository;
    private final Map<String, ChannelTopic> topics;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;


    public MessageGroup createChat(MessageGroupSaveDto messageGroupSaveDto) {
        MessageGroup messageGroup = MessageGroup.builder()
                .messageGroupName(messageGroupSaveDto.getChatName())
                .isOpenChat(messageGroupSaveDto.getIsOpenChat())
                .build();

        messageGroupRepository.save(messageGroup);

        String chatId = "room" + messageGroup.getId();

        if(!topics.containsKey(chatId)) {
            ChannelTopic topic = new ChannelTopic(chatId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(chatId, topic);
        }

        return messageGroup;
    }

    @Transactional(readOnly = true)
    public List<MessageGroupDto> searchChatName(String chatName) {
        List<MessageGroup> messageGroupList = messageGroupRepository.findByMessageGroupNameContaining(chatName);
        List<MessageGroupDto> messageGroupDtoList = new ArrayList<>();

        for (MessageGroup messageGroup : messageGroupList) {
            messageGroupDtoList.add(new MessageGroupDto(messageGroup));
        }

        return messageGroupDtoList;
    }

    @Transactional(readOnly = true)
    public MessageGroup getMessageGroupEntity(Long id) {
        return messageGroupRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("MessageGroup ID : %d 로 찾을 수 없습니다.", id));
        });
    }

}
