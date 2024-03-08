package com.example.livechat.controller.api;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.HttpResponseDto;
import com.example.livechat.domain.dto.MessageDto;
import com.example.livechat.domain.dto.MessagePushRedisDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.service.MessageService;
import com.example.livechat.service.redis.RedisPublisher;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageApiController {

    private final MessageService messageService;
//    private final RedisPublisher redisPublisher;
    private final RedisTemplate redisTemplate;

//    @PostMapping
//    public HttpResponseDto<?> saveMessage(@CurrentMember Member member,
//                                          @RequestBody@Valid final MessageSaveDto messageSaveDto,
//                                          BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
//            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, errorMessage, null);
//        }
//
//        messageService.saveMessage(messageSaveDto, member);
//
//        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "메시지 생성 성공", null);
//    }

    @GetMapping("/chats/{chatId}/messages")
    public HttpResponseDto<List<MessagePushRedisDto>> getChatMessageList(@PathVariable("chatId") final Long chatId,
                                                                         @CurrentMember Member member) {
        List<MessagePushRedisDto> messageList = messageService.getChatMessageList(chatId, member);

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "대화방 메시지 로드 성공", messageList);
    }


}
