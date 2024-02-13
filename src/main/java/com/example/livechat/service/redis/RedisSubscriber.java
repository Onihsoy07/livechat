package com.example.livechat.service.redis;

import com.example.livechat.domain.dto.MessageSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final ObjectMapper mapper;
    private final SimpMessageSendingOperations messageSendingOperations;


    public void sendMessage(String message) {
        try {
            MessageSaveDto chatMessage = mapper.readValue(message, MessageSaveDto.class);

            messageSendingOperations.convertAndSend("/sub/chats/" + chatMessage.getChatId(), chatMessage);
        } catch (Exception e) {
            log.error("RedisSubscriber onMessage Exception", e);
        }
    }
}
