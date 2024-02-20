package com.example.livechat.repository;

import com.example.livechat.domain.entity.MemberChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberChatRepository extends JpaRepository<MemberChat, Long> {

    @Query("select mmg from MemberChat mmg join fetch mmg.chat where mmg.member.username = :username")
    List<MemberChat> findByMember_Username(@Param("username") String username);

    Optional<MemberChat> findByMember_IdAndChat_id(Long memberId, Long chatId);

}
