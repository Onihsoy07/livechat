package com.example.livechat.service;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.domain.dto.MessageDto;
import com.example.livechat.domain.dto.MessagePushRedisDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.enumerate.MessageType;
import com.example.livechat.exception.NotContainsUserChatException;
import com.example.livechat.repository.AttachRepository;
import com.example.livechat.repository.MessageRepository;
import com.example.livechat.service.redis.RedisPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MemberChatService memberChatService;
    private final JwtProvider jwtProvider;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private static final String CHAT_ROOM = "CHAT_ROOM";
    private HashOperations<String, Long, List<MessagePushRedisDto>> opsHash;

    @PostConstruct
    private void init() {
        opsHash = redisTemplate.opsForHash();
    }

    public void messageResolver(MessageSaveDto messageSaveDto, String jwtToken) {
        Member sender = jwtProvider.getMember(jwtToken);

        if (!memberChatService.checkMyChat(messageSaveDto.getChatId(), sender.getId())) {
            throw new NotContainsUserChatException("해당 유저는 이 채팅방에 없습니다.");
        }

        chatService.enterChat(messageSaveDto.getChatId());

        saveMessage(messageSaveDto, sender);
    }

    public MessagePushRedisDto saveMessage(MessageSaveDto messageSaveDto, Member sender) {
        Chat chat = chatService.getChatEntity(messageSaveDto.getChatId());
        Message message = null;

        switch (messageSaveDto.getMessageType()) {
            case MESSAGE -> message = Message.builder()
                    .messageType(MessageType.MESSAGE)
                    .sender(sender)
                    .chat(chat)
                    .contents(messageSaveDto.getMessage())
                    .build();
            case ENTER -> message = Message.builder()
                    .messageType(MessageType.ENTER)
                    .sender(sender)
                    .chat(chat)
                    .contents(messageSaveDto.getMessage())
                    .build();
            case ATTACH -> message = Message.builder()
                    .messageType(MessageType.ATTACH)
                    .sender(sender)
                    .chat(chat)
                    .contents(messageSaveDto.getMessage())
                    .attach(messageSaveDto.getAttach())
                    .build();
            default -> throw new IllegalArgumentException("MessageType 값이 이상합니다. messageType : " + messageSaveDto.getMessageType());
        }

        messageRepository.save(message);

        MessagePushRedisDto messagePushRedisDto = new MessagePushRedisDto(message);

        redisPublisher.publisher(channelTopic, messagePushRedisDto);

        List<MessagePushRedisDto> messageDtoListInCache = opsHash.get(CHAT_ROOM, messageSaveDto.getChatId());
        List<MessagePushRedisDto> newMessageDtoList;
        if (messageDtoListInCache == null) {
            newMessageDtoList = new ArrayList<>();
        } else {
            newMessageDtoList = new ArrayList<>(messageDtoListInCache);
        }

        newMessageDtoList.add(messagePushRedisDto);
        opsHash.put(CHAT_ROOM, messageSaveDto.getChatId(), newMessageDtoList);

        return messagePushRedisDto;
    }

    @Transactional(readOnly = true)
    public List<MessagePushRedisDto> getChatMessageList(Long chatId, Member member) {
        List<MessagePushRedisDto> messageDtoListInCache = opsHash.get(CHAT_ROOM, chatId);

        if (messageDtoListInCache != null) {
            return messageDtoListInCache;
        }

        List<Message> messageList = messageRepository.findByMessageGroup_Id(chatId);
        List<MessagePushRedisDto> messageDtoList = new ArrayList<>();

        for (Message message : messageList) {
            message.viewMessage(member);
            messageDtoList.add(new MessagePushRedisDto(message));
        }

        opsHash.put(CHAT_ROOM, chatId, messageDtoList);

        return messageDtoList;
    }

}
