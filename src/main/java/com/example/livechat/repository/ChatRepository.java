package com.example.livechat.repository;

import com.example.livechat.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByChatNameContaining(String chatName);

}
