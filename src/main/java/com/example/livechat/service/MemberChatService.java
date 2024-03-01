package com.example.livechat.service;

import com.example.livechat.domain.dto.ChatDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.MemberChat;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.enumerate.MessageType;
import com.example.livechat.repository.MemberChatRepository;
import com.example.livechat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberChatService {

    private final MemberChatRepository memberChatRepository;
//    private final MessageRepository messageRepository;

    public void createMemberMessageGroup(Member member, Chat chat) {
        MemberChat memberChat = MemberChat.builder()
                .member(member)
                .chat(chat)
                .build();
//        Message message = Message.builder()
//                .sender(memberChat.getMember())
//                .chat(memberChat.getChat())
//                .messageType(MessageType.ENTER)
//                .contents(member.getUsername() + "님이 입장하셨습니다.")
//                .build();

        memberChatRepository.save(memberChat);
//        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatDto> getMyChatList(String username) {
        List<MemberChat> memberChatList = memberChatRepository.findByMember_Username(username);
        List<ChatDto> chatList = new ArrayList<>();

        for (MemberChat memberChat : memberChatList) {
            chatList.add(new ChatDto(memberChat.getChat()));
        }

        return chatList;
    }

    @Transactional(readOnly = true)
    public Boolean checkMyChat(Long chatId, Long memberId) {
        Optional<MemberChat> result = memberChatRepository.findByMember_IdAndChat_id(memberId, chatId);
        return result.isPresent();
    }


}
