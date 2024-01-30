package com.example.livechat.filter;

import com.example.livechat.auth.JwtProvider;
import com.example.livechat.auth.PrincipalDetails;
import com.example.livechat.domain.constant.JwtConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // JWT 토큰 헤더를 추가하지 않아도 해당 필터는 통과는 할 수 있지만, 결국 시큐리티단에서 세션 값 검증에 실패함.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            // 토큰이 존재함
            log.debug("디버그 : 토큰이 존재함");

            String token = request.getHeader(JwtConst.HEADER).replace(JwtConst.TOKEN_PREFIX, "");
            PrincipalDetails principal = JwtProvider.verify(token);
            log.debug("디버그 : 토큰이 검증이 완료됨");

            // 임시 세션 (UserDetails 타입 or username)
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal.getUsername(), null, principal.getAuthorities()); // id, role 만 존재
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 임시 세션이 생성됨");
        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtConst.HEADER);
        if (header == null || !header.startsWith(JwtConst.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}