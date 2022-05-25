package com.ticp.mapper;

import com.ticp.dto.PasswordDTO;
import com.ticp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordMapper implements Mapper<User, PasswordDTO>
{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User toModel(PasswordDTO passwordDTO, User model)
    {
        model.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        return model;
    }
}
