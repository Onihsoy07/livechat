package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.MemberSaveDto;
import com.example.livechat.domain.entity.HttpResponseDto;
import com.example.livechat.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public HttpResponseDto<?> saveMember(HttpServletRequest request,
                                         @RequestBody @Valid MemberSaveDto memberSaveDto,
                                         BindingResult bindingResult) {
        //Vue 프론트에서 요청 시 클라이언트 URL 확인(0:0:0:0:0:0:0:1)
        //Spring Security authorizeHttpRequests에 host가 달라서 안걸림?
        String requestUrl = request.getRemoteAddr().toString();

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

}