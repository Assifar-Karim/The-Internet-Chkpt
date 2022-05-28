package com.ticp.service;

import com.ticp.dto.UserPrincipalDTO;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.mapper.UserPrincipalMapper;
import com.ticp.model.User;
import com.ticp.oauth2.user.OAuth2UserInfo;
import com.ticp.oauth2.user.OAuth2UserInfoFactory;
import com.ticp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService
{
    private static Logger logger = LogManager.getLogger(OAuth2UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConcreteMapperFactory mapperFactory;

    public OAuth2UserService(UserRepository userRepository, ConcreteMapperFactory mapperFactory)
    {
        this.userRepository = userRepository;
        this.mapperFactory = mapperFactory;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try
        {
            OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo
                    (userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
            if(oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().equals(""))
            {
                logger.error("Email not found from OAuth provider");
                throw new OAuth2AuthenticationException("Email not found from OAuth provider");
            }
            String email = oAuth2UserInfo.getEmail();
            User user = userRepository.findByEmail(email);
            if(user == null)
            {
                user = new User();
                user.setProvider(userRequest.getClientRegistration().getRegistrationId());
                user.setUsername(email.substring(0,email.indexOf("@")));
                user.setEmail(email);
                user.setActive(true);

            }
            else
            {
                if(!user.getProvider().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId()))
                {
                    logger.error("You signed up with your " + user.getProvider() + " account. Please use your " + user.getProvider()
                            + "instead");
                    throw new OAuth2AuthenticationException(
                            "You signed up with your " + user.getProvider() + " account. Please use your " + user.getProvider()
                                    + "instead");
                }
            }
            userRepository.save(user);
            UserPrincipalMapper userPrincipalMapper = (UserPrincipalMapper) mapperFactory.getMapper(UserPrincipalDTO.class);
            return userPrincipalMapper.toDTO(user, oAuth2User.getAttributes());
        }catch(OAuth2AuthenticationException e)
        {
            throw e;
        }catch (Exception e1)
        {
            throw new InternalAuthenticationServiceException(e1.getMessage(), e1.getCause());
        }

    }
}
