package com.example.livechat.filter;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.auth.PrincipalDetails;
import com.example.livechat.domain.constant.JwtConst;
import com.example.livechat.domain.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    // 인증 관리자를 설정하고, 로그인 처리 URL을 설정
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // 로그인 시도를 처리한다.
    // 사용자가 제공한 로그인 정보 DTO를 받아, UsernamePasswordAuthenticationToken 생성
    // 이를 AuthenticationManager에 전달하여 인증시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();
            MemberDto memberDto = om.readValue(request.getInputStream(), MemberDto.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    memberDto.getUsername(), memberDto.getPassword());


            // UserDetailsService의 loadUserByUsername 호출하며 인증과정 시작
            // JWT를 쓴다 하더라도, 컨트롤러 진입을 하면 시큐리티의 권한체크, 인증체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request하고, response하면 끝!!
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (Exception e) {
            // InternalAuthenticationServiceException -> config에서 설정한 예외를 호출한다.
            // unsuccessfulAuthentication 호출함
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패 (attemptAuthentication 실패)
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
//        CustomResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);
        throw new IllegalArgumentException("로그인 실패");
    }

    // 로그인 성공 (attemptAuthentication 성공)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출됨");

        // 인증 정보를 토대로 JWT토큰 생성하고 반환
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JwtProvider.create(principal);
        response.addHeader(JwtConst.HEADER, jwtToken);

        MemberDto loginRespDto = new MemberDto(principal.getMember());
//        CustomResponseUtil.success(response, loginRespDto);
    }

}
