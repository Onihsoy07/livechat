package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordCheckDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
