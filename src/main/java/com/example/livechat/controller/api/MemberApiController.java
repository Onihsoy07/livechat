package com.example.livechat.controller.api;

import com.example.livechat.domain.dto.MemberSaveDto;
import com.example.livechat.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> saveMember(@RequestBody @Valid MemberSaveDto memberSaveDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError error = bindingResult.getAllErrors().get(0);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        if (memberService.duplicateUsernameCheck(memberSaveDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복 username이 있습니다.");
        }

        memberService.saveMember(memberSaveDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
