package com.example.livechat.service;

import com.example.livechat.domain.dto.ChatDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.MemberChat;
import com.example.livechat.domain.entity.Message;
import com.example.livechat.domain.enumerate.MessageType;
import com.example.livechat.repository.ChatRepository;
import com.example.livechat.repository.MemberChatRepository;
import com.example.livechat.repository.MemberRepository;
import com.example.livechat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
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
    private final ChatService chatService;
    private final MemberService memberService;

    public void createMemberMessageGroup(Long memberId, Chat chat) {
        Member member = memberService.getMemberEntity(memberId);

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

    @Transactional(readOnly = true)
    public Boolean checkMyChat(Long chatId, Long memberId) {
        Optional<MemberChat> result = memberChatRepository.findByMember_IdAndChat_id(memberId, chatId);
        return result.isPresent();
    }

    public void leaveChat(Long chatId, Long memberId) {
        memberChatRepository.leaveChat(chatId, memberId);
        List<MemberChat> membersInChat = memberChatRepository.findByChat_Id(chatId);

        if (membersInChat.size() == 0) {
            chatService.closeChat(chatId);
        }
    }


}
