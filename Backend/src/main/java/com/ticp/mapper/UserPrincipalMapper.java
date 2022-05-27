package com.ticp.mapper;

import com.ticp.dto.UserPrincipalDTO;
import com.ticp.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class UserPrincipalMapper implements Mapper<User, UserPrincipalDTO>
{
    @Override
    public UserPrincipalDTO toDTO(User model, Object object)
    {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(model.getRole()));
        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO(
                model.getId(),
                model.getEmail(),
                model.getPassword(),
                authorities
        );
        userPrincipalDTO.setAttributes((Map<String, Object>) object);
        return userPrincipalDTO;
    }
}
