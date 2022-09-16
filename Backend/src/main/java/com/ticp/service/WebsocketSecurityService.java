package com.ticp.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface WebsocketSecurityService
{
    UsernamePasswordAuthenticationToken getAuthenticated(String token);
}
