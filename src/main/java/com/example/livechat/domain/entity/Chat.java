package com.example.livechat.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "live_chat")
public class Chat extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = false)
    private String chatName;

    @Column(nullable = false, unique = false)
    private Boolean isOpenChat;

    @Builder
    public Chat(String chatName, Boolean isOpenChat) {
        this.chatName = chatName;
        this.isOpenChat = isOpenChat;
    }

}
