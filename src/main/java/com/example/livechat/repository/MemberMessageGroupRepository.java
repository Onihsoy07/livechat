package com.example.livechat.repository;

import com.example.livechat.domain.entity.MemberMessageGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberMessageGroupRepository extends JpaRepository<MemberMessageGroup, Long> {

    List<MemberMessageGroup> findByMember_Username(String username);

}
