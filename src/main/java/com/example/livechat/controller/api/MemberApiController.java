package com.example.livechat.controller.api;

import com.example.livechat.annotation.CurrentMember;
import com.example.livechat.domain.dto.HttpResponseDto;
import com.example.livechat.domain.dto.MemberDto;
import com.example.livechat.domain.dto.MemberPasswordCheckDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.exception.NotMatchMemberPasswordException;
import com.example.livechat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/recheck")
    public HttpResponseDto<Long> memberPasswordReCheck(@RequestBody final MemberPasswordCheckDto memberPasswordCheckDto,
                                                       @CurrentMember final Member member) {
        Long memberId = null;

        try {
            memberId = memberService.passwordCheck(memberPasswordCheckDto, member);
        } catch (NotMatchMemberPasswordException e) {
            return new HttpResponseDto<>(HttpStatus.BAD_REQUEST.value(), false, e.getLocalizedMessage(), null);
        } catch (Exception e) {
            return new HttpResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, e.getLocalizedMessage(), null);
        }

        return new HttpResponseDto<>(HttpStatus.OK.value(), true, "비밀번호 확인 완료", memberId);
    }


    @GetMapping("/{memberId}")
    public HttpResponseDto<MemberDto> getMemberInfo(@PathVariable("memberId") final Long memberId,
                                                    @CurrentMember final Member member) {
        if (member.getId() == memberId) {
            MemberDto memberDto = new MemberDto(member);
            return new HttpResponseDto<>(HttpStatus.OK.value(), true, "유저 정보 조회", memberDto);
        }
        // 유저 PK값을 알 수 있기 때문에 404로 응답 수정
//            return new HttpResponseDto<>(HttpStatus.FORBIDDEN.value(), false, "권한 없음", null);
        return new HttpResponseDto<>(HttpStatus.NOT_FOUND.value(), false, "페이지 없음", null);
    }


}