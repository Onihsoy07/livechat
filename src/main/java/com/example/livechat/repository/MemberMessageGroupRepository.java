package com.example.livechat.repository;

import com.example.livechat.domain.entity.MemberMessageGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberMessageGroupRepository extends JpaRepository<MemberMessageGroup, Long> {

    @Query("select mmg from MemberMessageGroup mmg join fetch mmg.messageGroup where mmg.member.username like concat('%', :username, '%')")
    List<MemberMessageGroup> findByMember_Username(@Param("username") String username);

}
