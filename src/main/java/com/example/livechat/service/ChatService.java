package com.example.livechat.service;

import com.example.livechat.domain.dto.ChatDto;
import com.example.livechat.domain.dto.ChatSaveDto;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.repository.ChatRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final Map<String, ChannelTopic> topics;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;


    public Chat createChat(ChatSaveDto chatSaveDto) {
        Chat chat = Chat.builder()
                .chatName(chatSaveDto.getChatName())
                .isOpenChat(chatSaveDto.getIsOpenChat())
                .build();

        chatRepository.save(chat);

        String chatId = "room" + chat.getId();

        if(!topics.containsKey(chatId)) {
            ChannelTopic topic = new ChannelTopic(chatId);
//            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(chatId, topic);
        }

        return chat;
    }

    @Transactional(readOnly = true)
    public List<ChatDto> searchChatName(String chatName) {
        List<Chat> chatList = chatRepository.findByChatNameContaining(chatName);
        List<ChatDto> chatDtoList = new ArrayList<>();

        for (Chat chat : chatList) {
            chatDtoList.add(new ChatDto(chat));
        }

        return chatDtoList;
    }

    @Transactional(readOnly = true)
    public Chat getChatEntity(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("Chat ID : %d 로 찾을 수 없습니다.", id));
        });
    }

}
