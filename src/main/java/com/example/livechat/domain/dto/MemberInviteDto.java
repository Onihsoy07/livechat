package com.example.livechat.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInviteDto {

    @NotBlank(message = "MemberId를 입력해주세요.")
    private Long memberId;

    @NotBlank(message = "username를 입력해주세요.")
    private String username;

}
