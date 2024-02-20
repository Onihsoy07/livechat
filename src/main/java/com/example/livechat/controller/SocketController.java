package com.example.livechat.controller;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.MessagePushRedisDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.service.MessageService;
import com.example.livechat.service.redis.RedisPublisher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Enumeration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SocketController {

    private final MessageService messageService;
    private final RedisPublisher redisPublisher;

//    @MessageMapping("/api/chat/{chatId}")
//    @SendTo("/send")
//    public MessagePushRedisDto sendMessage(MessageSaveDto messageSaveDto,
//                                           @DestinationVariable("chatId") Long chatId,
//                                           @Header("Authentication") String token) {
//        MessagePushRedisDto messagePushRedisDto = messageService.saveMessage(messageSaveDto, token);
//
//        log.info("messagePushRedisDto : {}", messagePushRedisDto);
//
//        redisPublisher.publisher(ChannelTopic.of("chat" + chatId), messagePushRedisDto);
//        redisTemplate.convertAndSend(ChannelTopic.of("chat" + chatId).getTopic(), messagePushRedisDto);
//        return messagePushRedisDto;
//    }

}
