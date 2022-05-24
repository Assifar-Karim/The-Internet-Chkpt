package com.ticp.mapper;

import com.ticp.dto.PasswordDTO;
import com.ticp.model.User;

// NOTE (KARIM) : Finish implementing this class when you start the resetting mechanism
public class PasswordMapper implements Mapper<User, PasswordDTO>
{
    public static PasswordMapper instance;

    private PasswordMapper()
    {}

    public static PasswordMapper getInstance()
    {
        if(instance == null)
        {
            instance = new PasswordMapper();
        }
        return instance;
    }
    // NOTE (KARIM) : Add BCRYPT Encryption
    @Override
    public User toModel(PasswordDTO passwordDTO, User model)
    {
        model.setPassword(passwordDTO.getNewPassword());
        return model;
    }
}
