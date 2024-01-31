package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDto {

    @NotBlank(message = "username을 입력해주세요.")
    @Size(min = 3, max = 15, message = "username의 범위는 3~15자리 입니다.")
    private String username;

    @NotBlank(message = "password을 입력해주세요.")
    @Size(min = 3, max = 30, message = "password의 범위는 3~30자리 입니다.")
    private String password;

}
