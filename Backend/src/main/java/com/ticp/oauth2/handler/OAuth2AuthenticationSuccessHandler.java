package com.ticp.oauth2.handler;

import com.ticp.dto.UserPrincipalDTO;
import com.ticp.error.BadRequestException;
import com.ticp.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.ticp.util.CookieUtil;
import com.ticp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.ticp.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${authorizedRedirectUrls}")
    private String[] authorizedRedirectUrls;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException
    {
        Cookie cookie = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);
        if(cookie != null && !isAuthorizedRedirectUri(cookie.getValue()))
        {
            throw new BadRequestException("Unauthorized Redirect URI, we can't proceed with the authentication");
        }
        String targetUrl = cookie != null ? cookie.getValue() : getDefaultTargetUrl();
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenUtil.generateJwtToken(user.getUsername(), user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),request);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token",token)
                .build().toUriString();
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response)
    {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri)
    {
        URI clientRedirectUri = URI.create(uri);
        for(String url : authorizedRedirectUrls)
        {
            URI authorizedURI = URI.create(url);
            if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) &&
                authorizedURI.getPort() == clientRedirectUri.getPort())
            {
                return true;
            }
        }
        return false;
    }
}
