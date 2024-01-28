package com.example.livechat.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.livechat.domain.constant.JwtVo;
import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.enumerate.Role;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtProvider {

    // 토큰 생성
    public static String create(PrincipalDetails principal) {
        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.EXPIRATION_TIME))
                .withClaim("id", principal.getMember().getId())
                .withClaim("role", principal.getMember().getRole() + "")
                .sign(Algorithm.HMAC512(JwtVo.SECRET));
        return JwtVo.TOKEN_PREFIX + jwtToken;
    }


    // 토큰 검증
    // return 되는 PrincipalDetails 객체를 강제로 시큐리티 세션에 직접 주입할 예정
    public static PrincipalDetails verify(String token) {
        // 시크릿 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET))
                .build()
                .verify(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder()
                .id(id)
                .role(Role.valueOf(role))
                .build();

        return new PrincipalDetails(member);
    }

}
