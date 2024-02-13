package com.example.livechat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    // MessageChannel로 Message를 보내기 전과 후에 추가적인 로직 가능
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            log.info("CONNECT");
        }else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            log.info("SUBSCRIBE");
        }else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
            log.info("DISCONNECT");
        }

        return message;
    }

}
