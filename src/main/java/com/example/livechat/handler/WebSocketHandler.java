package com.example.livechat.handler;

import com.example.livechat.domain.dto.MessageDto;
import com.example.livechat.domain.dto.MessageSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final Map<Long, Set<WebSocketSession>> chatSessionMap = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("WS payload : {}", payload);

        MessageSaveDto messageDto = mapper.readValue(payload, MessageSaveDto.class);
        log.info("WS messageDto : {}", messageDto);

        Long chatId = messageDto.getChatId();
        if (!chatSessionMap.containsKey(chatId)) {
            chatSessionMap.put(chatId, new HashSet<>());
        }
        Set<WebSocketSession> chatWebSocketSessions = chatSessionMap.get(chatId);

        chatWebSocketSessions.add(session);

        removeClosedSession(chatWebSocketSessions);

        sendMessageToChat(messageDto, chatWebSocketSessions);

        log.info("대화방ID {}, 접속 세션 수 : {}", chatId, chatWebSocketSessions.size());
    }

    // 클라이언트 접속 시 호출 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
        log.info("연결 세션 수 : {}", sessions.size());
    }

    // 클라이언트 접속 해제 시 호출 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
        log.info("연결 세션 수 : {}", sessions.size());
    }

    // close 된 세션 제거
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChat(MessageSaveDto messageSaveDto, Set<WebSocketSession> chatWebSocketSessions) {
        for (WebSocketSession chatWebSocketSession : chatWebSocketSessions) {
            sendMessage(chatWebSocketSession, messageSaveDto);
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("WS IOException", e);
        } catch (IllegalStateException e) {
            throw e;
        }
    }

}
