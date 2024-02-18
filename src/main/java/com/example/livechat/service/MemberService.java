package com.example.livechat.service;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.domain.dto.MemberDto;
import com.example.livechat.domain.dto.MemberLoginDto;
import com.example.livechat.domain.dto.MemberSaveDto;
import com.example.livechat.domain.dto.TokenDto;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.enumerate.Role;
import com.example.livechat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
// 트랜잭션 사용하지 않는 매서드가 있음
// 사용하면 아래 DEBUG 발생
// Closing JPA EntityManager [SessionImpl(1498918044<open>)] after transaction
// Not closing pre-bound JPA EntityManager after transaction
//@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    @Transactional
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
    public String login(MemberLoginDto memberLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken;
        Authentication authenticate;
        String token = null;

        try {
            authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(), memberLoginDto.getPassword());

            authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            token = jwtProvider.createToken(authenticate);
        } catch (Exception e) {
            log.error("login Error : {}", e);
        }

        return token;
    }

    public Boolean tokenAvailableCheck(TokenDto tokenDto) {
        boolean tokenAvailable = jwtProvider.validateToken(tokenDto.getToken());

        return tokenAvailable;
    }

    @Transactional(readOnly = true)
    public MemberDto getMember(String username) {
        return new MemberDto(getMemberByUsername(username));
    }

    @Transactional(readOnly = true)
    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("Member username %s로 찾을 수 없습니다.", username));
        });
    }

    @Transactional(readOnly = true)
    public Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new IllegalArgumentException(String.format("Member ID %d로 찾을 수 없습니다.", memberId));
        });
    }

}
