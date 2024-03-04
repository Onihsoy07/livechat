package com.example.livechat.service;

import com.example.livechat.domain.dto.ChatDto;
import com.example.livechat.domain.dto.ChatSaveDto;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.repository.ChatRepository;
import com.example.livechat.service.redis.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    // redis
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate redisTemplate;
    private Map<Long, ChannelTopic> topics;
    private HashOperations<String, String, Chat> opsHashChat;
    private HashOperations<String, String, String> opsHashEnterInfo;

    private static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String ENTER_INFO = "ENTER_INFO";


    @PostConstruct
    private void init() {
        opsHashChat = redisTemplate.opsForHash();
        opsHashEnterInfo = redisTemplate.opsForHash();

        topics = new HashMap<>();
    }

    public Chat createChat(ChatSaveDto chatSaveDto) {
        Chat chat = Chat.builder()
                .chatName(chatSaveDto.getChatName())
                .isOpenChat(chatSaveDto.getIsOpenChat())
                .build();

        chatRepository.save(chat);

        opsHashChat.put(ENTER_INFO, chat.getId().toString(), chat);

        return chat;
    }

    public void enterChat(Long chatId) {
        ChannelTopic channelTopic = topics.get(chatId);

        if (channelTopic == null) {
            channelTopic = new ChannelTopic("chat" + chatId);
        }

        redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic);
        log.info("chatId {} 에 접속, channelTopic : {}", chatId, channelTopic);
        topics.put(chatId, channelTopic);
    }

    public ChannelTopic getTopic(Long chatId) {
        return topics.get(chatId);
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

    // 유저가 입장한 채팅방 ID와 유저 세션 ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        opsHashEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션 정보로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return opsHashEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션 정보와 맵핑된 채팅방 ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        opsHashEnterInfo.delete(ENTER_INFO, sessionId);
    }

}
