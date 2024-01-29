package com.example.livechat.service;

import com.example.livechat.domain.dto.MemberDto;
import com.example.livechat.domain.dto.MemberSaveDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.enumerate.Role;
import com.example.livechat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveMember(MemberSaveDto memberSaveDto) {
        Member member = Member.builder()
                .username(memberSaveDto.getUsername())
                .password(bCryptPasswordEncoder.encode(memberSaveDto.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    /**
     * 중복 username 있을 떄 return true
     * 중복 username 없을 떄 return false
     */
    @Transactional(readOnly = true)
    public Boolean duplicateUsernameCheck(String username) {
        return !memberRepository.findByUsername(username).isEmpty();
    }

    @Transactional(readOnly = true)
    public MemberDto getMember(String username) {
        return new MemberDto(getMemberByUsername(username));
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("Member username %s로 찾을 수 없습니다.", username));
        });
    }

}
