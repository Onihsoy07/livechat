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
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            MessageSaveDto chatMessage = mapper.readValue(publishMessage, MessageSaveDto.class);

            messageSendingOperations.convertAndSend("/sub/chats/" + chatMessage.getChatId(), chatMessage);
        } catch (Exception e) {
            log.error("RedisSubscriber onMessage Exception", e);
        }
    }
}
