package com.example.livechat.controller.api;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.*;
import com.example.livechat.domain.entity.Chat;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.enumerate.MessageType;
import com.example.livechat.service.MemberChatService;
import com.example.livechat.service.ChatService;
import com.example.livechat.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatApiController {

    private final ChatService chatService;
    private final MemberChatService memberChatService;
    private final MessageService messageService;

    @PostMapping("/chats")
    public HttpResponseDto<ChatDto> createChat(@RequestBody @Valid ChatSaveDto chatSaveDto,
                                         BindingResult bindingResult,
                                         @CurrentMember Member member,
                                         @RequestHeader("Authentication") String token) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, message, null);
        }

        Chat chat = chatService.createChat(chatSaveDto);
        memberChatService.createMemberMessageGroup(member.getId(), chat);

        enterMessage(chat.getId(), member, token);

        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "생성 성공", new ChatDto(chat));
    }

    @GetMapping("/members/{username}/chats")
    public HttpResponseDto<List<ChatDto>> getMyMessageGroupList(@PathVariable("username") String username,
                                                                @CurrentMember Member member) {
        if (!member.getUsername().equals(username)) {
            return new HttpResponseDto<>(HttpStatus.FORBIDDEN.value(), false, "권한 없음", null);
        }

        List<ChatDto> myMessageGroupList = memberChatService.getMyChatList(member.getUsername());

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "내 대화방 목록 로드", myMessageGroupList);
    }

    @GetMapping("/chats")
    public HttpResponseDto<List<ChatDto>> searchChatName(@RequestParam("name") String chatName) {
        List<ChatDto> messageGroupList = chatService.searchChatName(chatName);

        if (messageGroupList.isEmpty()) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "맞는 대화방이 존재하지 않습니다.", null);
        }

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "대화방 찾기 성공", messageGroupList);
    }

    @PostMapping("/chats/{chatId}")
    public HttpResponseDto<?> joinChat(@PathVariable("chatId") Long chatId,
                                       @RequestBody(required = false) final MemberInviteDto memberInviteDto,
                                       @CurrentMember Member member,
                                       @RequestHeader("Authentication") String token) {
        Chat chat = chatService.getChatEntity(chatId);

        if (memberInviteDto != null) {
            Boolean isJoinChat = memberChatService.checkMyChat(chatId, memberInviteDto.getMemberId());
            if (isJoinChat) {
                return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "이미 대화방에 참가한 유저입니다.", null);
            }

            memberChatService.createMemberMessageGroup(memberInviteDto.getMemberId(), chat);
            inviteMessage(chatId, member, memberInviteDto.getUsername(), token);
        } else {
            Boolean isJoinChat = memberChatService.checkMyChat(chatId, member.getId());
            if (isJoinChat) {
                return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "이미 참가한 대화방입니다.", null);
            }

            memberChatService.createMemberMessageGroup(member.getId(), chat);
            enterMessage(chatId, member, token);
        }

        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "대화방 참가", null);
    }

    @DeleteMapping("/chats/{chatId}")
    public HttpResponseDto<Null> leaveChat(@PathVariable("chatId") final Long chatId,
                                           @CurrentMember final Member member,
                                           @RequestHeader("Authentication") String token) {
        Boolean isEnterChat = memberChatService.checkMyChat(chatId, member.getId());

        if (!isEnterChat) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "유저가 참가한 대화방이 아닙니다.", null);
        }

        leaveMessage(chatId, member, token);
        memberChatService.leaveChat(chatId, member.getId());
        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "대화방 나가기", null);
    }

    private void enterMessage(Long chatId, Member member, String token) {
        MessageSaveDto messageSaveDto = new MessageSaveDto(chatId, member.getUsername() + "님이 입장하셨습니다.", MessageType.ENTER);
        messageService.messageResolver(messageSaveDto, token);
    }

    private void inviteMessage(Long chatId, Member member, String inviteUsername, String token) {
        MessageSaveDto messageSaveDto = new MessageSaveDto(chatId, member.getUsername() + "님이 " + inviteUsername + "님을 초대하셨습니다.", MessageType.ENTER);
        messageService.messageResolver(messageSaveDto, token);
    }

    private void leaveMessage(Long chatId, Member member, String token) {
        MessageSaveDto messageSaveDto = new MessageSaveDto(chatId, member.getUsername() + "님이 나가셨습니다.", MessageType.ENTER);
        messageService.messageResolver(messageSaveDto, token);
    }

}
