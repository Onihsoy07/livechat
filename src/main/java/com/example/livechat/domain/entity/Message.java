package com.example.livechat.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(schema = "live_chat")
public class Message extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = false)
    private String contents;

    @Column(nullable = true, unique = false)
    private List<Long> viewerMemberList = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Builder
    public Message(String contents, Member sender, Chat chat) {
        this.contents = contents;
        this.sender = sender;
        this.chat = chat;
    }

}
