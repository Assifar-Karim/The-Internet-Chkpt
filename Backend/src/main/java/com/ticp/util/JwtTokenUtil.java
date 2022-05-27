package com.ticp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtTokenUtil
{
    public static final int TOKEN_VALIDITY = 10 * 60 * 1000;
    public static final int REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(String username, List<?> roles, HttpServletRequest request)
    {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT
                .create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", roles)
                .sign(algorithm);
    }
    public String generateRefreshToken(User user, HttpServletRequest request)
    {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public void issueTokenResponse(String accessToken, String refreshToken, HttpServletResponse response)
            throws IOException
    {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    public void issueTokenVerificationErrorResponse(HttpServletResponse response, String message)
            throws IOException
    {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("status",HttpStatus.FORBIDDEN.toString());
        error.put("message", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
    public DecodedJWT verifyToken(String authorizationHeader)
    {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }
}
