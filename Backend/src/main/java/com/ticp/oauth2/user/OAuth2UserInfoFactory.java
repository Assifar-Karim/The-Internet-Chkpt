package com.ticp.oauth2.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.security.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory
{
    private static Logger logger = LogManager.getLogger(OAuth2UserInfoFactory.class);
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes)
    {
        if(registrationId.equalsIgnoreCase("google"))
        {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else
        {
            logger.warn("User tried to login with {} which isn't supported yet", registrationId);
            throw new OAuth2AuthenticationException("Login with " + registrationId + "is not supported yet !");
        }
    }
}
