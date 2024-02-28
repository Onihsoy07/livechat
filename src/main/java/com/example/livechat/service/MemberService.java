package com.example.livechat.service;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.domain.dto.*;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.enumerate.Role;
import com.example.livechat.exception.NotMatchMemberPasswordCheckException;
import com.example.livechat.exception.NotMatchMemberPasswordException;
import com.example.livechat.exception.SameMemberUpdatePasswordException;
import com.example.livechat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

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

    @Transactional(readOnly = true)
    public Long passwordCheck(MemberPasswordCheckDto memberPasswordCheckDto, Member member) {
        String memberPassword = member.getPassword();
        String memberCheckPassword = memberPasswordCheckDto.getPassword();

        if (bCryptPasswordEncoder.matches(memberCheckPassword, memberPassword)) {
            return member.getId();
        }

        throw new NotMatchMemberPasswordException("비밀번호가 다릅니다.");
    }

    public void updatePassword(MemberPasswordUpdateDto memberPasswordUpdateDto, Member member) {
        String password = memberPasswordUpdateDto.getPassword();
        String passwordCheck = memberPasswordUpdateDto.getPasswordCheck();

        if (!password.equals(passwordCheck)) {
            throw new NotMatchMemberPasswordCheckException("비밀번호와 비밀번호 재확인이 다릅니다.");
        }
        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new SameMemberUpdatePasswordException("현재 비밀번호와 같습니다.");
        }

        Member memberEntity = getMemberByUsername(member.getUsername());
        String encodePassword = bCryptPasswordEncoder.encode(password);

        memberEntity.updatePassword(encodePassword);
        updatePrincipal(memberEntity.getUsername(), password);
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

    // SecurityContextHolder.Context.Authentication 변경 (비밀번호 수정된 Authentication 저장)
    private void updatePrincipal(String username, String password) {
        SecurityContextHolder.clearContext();

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

}
