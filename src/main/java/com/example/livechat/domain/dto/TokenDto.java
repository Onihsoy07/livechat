package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @NotNull(message = "JWT 토큰이 없습니다.")
    private String token;

}
