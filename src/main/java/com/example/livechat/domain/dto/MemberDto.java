package com.example.livechat.domain.dto;

import com.example.livechat.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
