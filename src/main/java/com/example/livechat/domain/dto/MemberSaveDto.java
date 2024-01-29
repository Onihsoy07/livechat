package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSaveDto {

    @NotBlank(message = "username을 입력해주세요.")
    private String username;

    @NotBlank(message = "password을 입력해주세요.")
    private String password;

}
