package com.ticp.mapper;

import com.ticp.dto.UserDTO;
import com.ticp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDTO>
{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDTO toDTO(User model)
    {
        return new UserDTO(
                model.getUsername(),
                model.getEmail(),
                model.getPassword()
        );
    }

    @Override
    public User toModel(UserDTO userDTO)
    {
        return new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()));
    }
}
