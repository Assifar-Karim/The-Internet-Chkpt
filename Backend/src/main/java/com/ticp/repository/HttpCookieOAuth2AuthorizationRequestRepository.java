package com.ticp.repository;

import com.ticp.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>
{
    public static final String OAUTH2_AUTH_REQ_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    private static final int COOKIE_EXPIRE_SECONDS = 100;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request)
    {
        Cookie cookie = CookieUtil.getCookie(request, OAUTH2_AUTH_REQ_COOKIE_NAME);

        return cookie != null ? CookieUtil.deserialize(cookie,OAuth2AuthorizationRequest.class) : null;
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response)
    {
        if(authorizationRequest == null)
        {
            this.removeAuthorizationRequestCookies(request, response);
            return;
        }
        CookieUtil.addCookie(
                response,
                OAUTH2_AUTH_REQ_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if(!redirectUriAfterLogin.isBlank() && !redirectUriAfterLogin.equals("") && redirectUriAfterLogin != null)
        {
            CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request)
    {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response)
    {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTH_REQ_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
