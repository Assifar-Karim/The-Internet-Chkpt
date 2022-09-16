package com.ticp.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ticp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class WebsocketSecurityServiceImpl implements WebsocketSecurityService
{
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UsernamePasswordAuthenticationToken getAuthenticated(String token)
    {
        if(token == null || token.isEmpty())
        {
            throw new AuthenticationCredentialsNotFoundException("Authentication token was null or empty");
        }
        DecodedJWT decodedJWT = jwtTokenUtil.verifyToken("Bearer ".concat(token));
        Calendar calendar = Calendar.getInstance();
        if(decodedJWT.getExpiresAt().getTime() - calendar.getTime().getTime() <= 0)
        {
            throw new TokenExpiredException("Expired auth token");
        }
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        return new UsernamePasswordAuthenticationToken(
                decodedJWT.getSubject(),
                null,
                stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
