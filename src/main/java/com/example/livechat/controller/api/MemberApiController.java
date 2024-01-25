package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.MemberSaveDto;
import com.example.livechat.domain.entity.HttpResponseDto;
import com.example.livechat.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public HttpResponseDto<?> saveMember(@RequestBody @Valid MemberSaveDto memberSaveDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError error = bindingResult.getAllErrors().get(0);
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "바인딩 데이터 검증 에러", error);
        }

        if (memberService.duplicateUsernameCheck(memberSaveDto.getUsername())) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "중복 username이 있습니다.", null);
        }

        memberService.saveMember(memberSaveDto);
        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "사용자 저장 성공", null);
    }

}
