package com.example.livechat.repository;

import com.example.livechat.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m join fetch m.sender join fetch m.attach where m.chat.id = :chatId order by m.createAt asc")
    List<Message> findByMessageGroup_Id(@Param("chatId") Long chatId);

}
