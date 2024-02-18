package com.example.livechat.service;

import com.example.livechat.domain.dto.ChatDto;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.MemberChat;
import com.example.livechat.repository.MemberChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberChatService {

    private final MemberChatRepository memberChatRepository;

    public void createMemberMessageGroup(Member member, Chat chat) {
        MemberChat memberChat = MemberChat.builder()
                .member(member)
                .chat(chat)
                .build();

        memberChatRepository.save(memberChat);
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


}
