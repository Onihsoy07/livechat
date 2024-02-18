package com.example.livechat.repository;

import com.example.livechat.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c " +
            "where c.chatName like concat('%', :chatName, '%')" +
            "and c.isOpenChat = true")
    List<Chat> findByChatNameContaining(@Param("chatName") String chatName);

}
