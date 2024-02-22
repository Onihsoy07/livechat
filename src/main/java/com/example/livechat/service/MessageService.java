package com.example.livechat.service;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.domain.dto.MessageDto;
import com.example.livechat.domain.dto.MessagePushRedisDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.exception.NotContainsUserChat;
import com.example.livechat.repository.MessageRepository;
import com.example.livechat.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MemberService memberService;
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MemberChatService memberChatService;
    private final JwtProvider jwtProvider;
//    private final RedisTemplate redisTemplate;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic channelTopic;

    public void messageResolver(MessageSaveDto messageSaveDto, String jwtToken) {
        Member sender = jwtProvider.getMember(jwtToken);

        if (!memberChatService.checkMyChat(messageSaveDto.getChatId(), sender.getId())) {
            throw new NotContainsUserChat("해당 유저는 이 채팅방에 없습니다.");
        }

        saveMessage(messageSaveDto, sender);

    }

    public MessagePushRedisDto saveMessage(MessageSaveDto messageSaveDto, Member sender) {
        Chat chat = chatService.getChatEntity(messageSaveDto.getChatId());

        Message message = Message.builder()
                .sender(sender)
                .chat(chat)
                .contents(messageSaveDto.getMessage())
                .build();

        messageRepository.save(message);

        MessagePushRedisDto messagePushRedisDto = new MessagePushRedisDto(message);

//        redisTemplate.convertAndSend(ChannelTopic.of("chat" + messageSaveDto.getChatId()).getTopic(), messagePushRedisDto);
//        redisPublisher.publisher(ChannelTopic.of("chat" + messageSaveDto.getChatId()), messagePushRedisDto);
        redisPublisher.publisher(channelTopic, messagePushRedisDto);

        return messagePushRedisDto;
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getChatMessageList(Long chatId) {
        List<Message> messageList = messageRepository.findByMessageGroup_Id(chatId);
        List<MessageDto> messageDtoList = new ArrayList<>();

        for (Message message : messageList) {
            messageDtoList.add(new MessageDto(message));
        }

        return messageDtoList;
    }

}
