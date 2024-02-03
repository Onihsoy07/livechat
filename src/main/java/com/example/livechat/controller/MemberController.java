package com.example.livechat.controller;

import com.example.livechat.domain.dto.MemberDto;
import com.example.livechat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{username}")
    public String memberChatHome(@PathVariable(name = "username") final String username,
                                 Model model) {
        MemberDto memberDto = memberService.getMember(username);
        model.addAttribute("memberDto" ,memberDto);
        return "chat/home";
    }

}
