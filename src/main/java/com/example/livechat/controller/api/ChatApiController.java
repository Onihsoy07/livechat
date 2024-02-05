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

    @GetMapping
    private HttpResponseDto<List<MessageGroupDto>> getMyMessageGroupList(@CurrentMember Member member) {
        List<MessageGroupDto> myMessageGroupList = memberMessageGroupService.getMyMessageGroupList(member.getUsername());

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "내 대화방 목록 로드", myMessageGroupList);
    }

}
