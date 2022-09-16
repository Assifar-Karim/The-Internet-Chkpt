package com.ticp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthChannelInterceptor implements ChannelInterceptor
{
    @Autowired
    private WebsocketSecurityService websocketSecurityService;
    private static final String TOKEN_HEADER = "token";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel)
    {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if(accessor.getCommand() == StompCommand.CONNECT)
        {
            final String token = accessor.getFirstNativeHeader(TOKEN_HEADER);
            UsernamePasswordAuthenticationToken user = websocketSecurityService.getAuthenticated(token);
            accessor.setUser(user);
        }
        return message;
    }
}
