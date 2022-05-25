package com.ticp.filter;

import com.ticp.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;



public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil)
    {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException
    {
        User user = (User) authResult.getPrincipal();
        String accessToken = jwtTokenUtil.generateJwtToken(user.getUsername(), user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()), request);
        String refresh_token = jwtTokenUtil.generateRefreshToken(user, request);
        jwtTokenUtil.issueTokenResponse(accessToken, refresh_token, response);
    }
}
