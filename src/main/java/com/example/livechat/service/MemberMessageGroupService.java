package com.example.livechat.service;

import com.example.livechat.domain.entity.Member;
import com.example.livechat.domain.entity.MemberMessageGroup;
import com.example.livechat.domain.entity.MessageGroup;
import com.example.livechat.repository.MemberMessageGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberMessageGroupService {

    private final MemberMessageGroupRepository memberMessageGroupRepository;

    public void createMemberMessageGroup(Member member, MessageGroup messageGroup) {
        MemberMessageGroup memberMessageGroup = MemberMessageGroup.builder()
                .member(member)
                .messageGroup(messageGroup)
                .build();

        memberMessageGroupRepository.save(memberMessageGroup);
    }

}
