package com.example.livechat.service.redis;

import com.example.livechat.domain.dto.MessagePushRedisDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
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
public class RedisSubscriber implements MessageListener {

    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Object deserialize = redisTemplate.getStringSerializer().deserialize(message.getBody());
            String publishMessage = (String) deserialize;
            MessagePushRedisDto messageDto = objectMapper.readValue(publishMessage, MessagePushRedisDto.class);

            messageSendingOperations.convertAndSend("/sub/chat/" + messageDto.getChatDto().getId(), messageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void sendMessage(String message) {
//        try {
//            MessageSaveDto chatMessage = mapper.readValue(message, MessageSaveDto.class);
//
//            messageSendingOperations.convertAndSend("/sub/chats/" + chatMessage.getChatId(), chatMessage);
//        } catch (Exception e) {
//            log.error("RedisSubscriber onMessage Exception", e);
//        }
//    }
}
