package com.ticp.mapper;

import com.ticp.model.Checkpoint;
import com.ticp.dto.UserPrincipalDTO;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteMapperFactory implements MapperFactory
{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordMapper passwordMapper;
    @Autowired
    private CheckpointMapper checkpointMapper;
    @Autowired
    private UserPrincipalMapper userPrincipalMapper;
    @Override
    public Mapper<?, ?> getMapper(Class<?> obj)
    {
        if(obj == User.class)
        {
            return userMapper;
        }
        if(obj == PasswordToken.class)
        {
            return passwordMapper;
        }
        if(obj == Checkpoint.class)
        {
            return checkpointMapper;
        }
        if(obj == UserPrincipalDTO.class)
        {
            return userPrincipalMapper;
        }
        return null;
    }
}
