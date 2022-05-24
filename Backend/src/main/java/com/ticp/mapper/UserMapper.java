package com.ticp.mapper;

import com.ticp.dto.UserDTO;
import com.ticp.model.User;

public class UserMapper implements Mapper<User, UserDTO>
{
    public static UserMapper instance;

    private UserMapper()
    {}

    public static UserMapper getInstance()
    {
        if(instance == null)
        {
            instance = new UserMapper();
        }
        return instance;
    }

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
        // NOTE (KARIM) : The password needs to be encrypted using the BCRYPT norm
        return new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword());
    }
}
