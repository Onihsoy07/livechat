package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.*;
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
@RequestMapping("/api/auth")
public class AuthApiController {

    private final MemberService memberService;

    @PostMapping("/register")
    public HttpResponseDto<?> saveMember(@RequestBody @Valid MemberSaveDto memberSaveDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError error = bindingResult.getAllErrors().get(0);
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "Valid error", error);
        }

        if (memberService.duplicateUsernameCheck(memberSaveDto.getUsername())) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "중복 username이 있습니다.", null);
        }

        memberService.saveMember(memberSaveDto);
        return new HttpResponseDto<>(HttpStatus.CREATED.value(), true, "사용자 저장 성공", null);
    }

    @PostMapping("/login")
    public HttpResponseDto<TokenDto> login(@RequestBody MemberLoginDto memberLoginDto) {
        String token = memberService.login(memberLoginDto);

        if (token == null) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, "아이디 또는 비밀번호가 다릅니다.", new TokenDto(token));
        }

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "로그인 성공", new TokenDto(token));
    }

    @PostMapping("/available")
    public HttpResponseDto<MemberDto> tokenAvailableCheck(@RequestBody TokenDto tokenDto) {
        MemberDto memberDto = null;
        if (memberService.tokenAvailableCheck(tokenDto)) {
            memberDto = memberService.getMemberInfoInToken(tokenDto);
            return new HttpResponseDto<>(HttpStatus.OK.value(), true, null, memberDto);
        } else {
            return new HttpResponseDto<>(HttpStatus.UNAUTHORIZED.value(), false, null, memberDto);
        }
    }

}
