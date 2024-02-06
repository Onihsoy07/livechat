package com.example.livechat.repository;

import com.example.livechat.domain.entity.MessageGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageGroupRepository extends JpaRepository<MessageGroup, Long> {

    List<MessageGroup> findByMessageGroupNameContaining(String chatName);

}
