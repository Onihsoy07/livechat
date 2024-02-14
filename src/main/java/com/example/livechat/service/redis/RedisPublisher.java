package com.example.livechat.service.redis;

import com.example.livechat.domain.dto.MessagePushRedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publisher(ChannelTopic topic, MessagePushRedisDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }


}
