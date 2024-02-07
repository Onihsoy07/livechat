package com.example.livechat.controller.api;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.HttpResponseDto;
import com.example.livechat.domain.dto.MessageGroupDto;
import com.example.livechat.domain.dto.MessageGroupSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.MessageGroup;
import com.example.livechat.service.MemberMessageGroupService;
import com.example.livechat.service.MessageGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {

    private final MessageGroupService messageGroupService;
    private final MemberMessageGroupService memberMessageGroupService;

    @PostMapping
    public HttpResponseDto<?> createChat(@RequestBody @Valid MessageGroupSaveDto messageGroupSaveDto,
                                         BindingResult bindingResult,
                                         @CurrentMember Member member) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, message, null);
        }

        MessageGroup messageGroup = messageGroupService.createChat(messageGroupSaveDto);
        memberMessageGroupService.createMemberMessageGroup(member, messageGroup);

        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "생성 성공", null);
    }

    @GetMapping("/{username}")
    public HttpResponseDto<List<MessageGroupDto>> getMyMessageGroupList(@PathVariable("username") String username,
                                                                        @CurrentMember Member member) {
        if (!member.getUsername().equals(username)) {
            return new HttpResponseDto<>(HttpStatus.FORBIDDEN.value(), false, "권한 없음", null);
        }

        List<MessageGroupDto> myMessageGroupList = memberMessageGroupService.getMyMessageGroupList(member.getUsername());

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "내 대화방 목록 로드", myMessageGroupList);
    }

    @GetMapping
    public HttpResponseDto<List<MessageGroupDto>> searchChatName(@RequestParam("name") String chatName) {
        List<MessageGroupDto> messageGroupList = messageGroupService.searchChatName(chatName);

        if (messageGroupList.isEmpty()) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "맞는 대화방이 존재하지 않습니다.", null);
        }

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "대화방 찾기 성공", messageGroupList);
    }

    @PostMapping("/{chatId}")
    public HttpResponseDto<?> joinChat(@PathVariable("chatId") Long chatId,
                                       @CurrentMember Member member) {
        MessageGroup messageGroup = messageGroupService.getMessageGroupEntity(chatId);
        memberMessageGroupService.createMemberMessageGroup(member, messageGroup);

        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "생성 성공", null);
    }

}
