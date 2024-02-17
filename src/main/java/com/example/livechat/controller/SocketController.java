package com.example.livechat.controller;

import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Enumeration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SocketController {

    private final MessageService messageService;

    @MessageMapping("/receive")
    @SendTo("/send")
    public MessageSaveDto sendMessage(MessageSaveDto messageSaveDto) {
//        Long chatId = messageSaveDto.getChatId();

//        MessagePushRedisDto messagePushRedisDto = messageService.saveMessage(messageSaveDto, member, chatId);

//        log.info("messagePushRedisDto : {}", messagePushRedisDto);

//        redisPublisher.publisher(ChannelTopic.of("chat" + chatId), messagePushRedisDto);
//        redisTemplate.convertAndSend(ChannelTopic.of("chat" + chatId).getTopic(), messagePushRedisDto);
        return messageSaveDto;
    }

}
