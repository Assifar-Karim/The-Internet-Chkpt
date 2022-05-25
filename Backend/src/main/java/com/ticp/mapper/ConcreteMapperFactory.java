package com.ticp.mapper;

import com.ticp.model.Checkpoint;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteMapperFactory implements MapperFactory
{
    @Autowired
    private CheckpointMapper checkpointMapper;
    @Override
    public Mapper<?, ?> getMapper(Class<?> obj)
    {

        if(obj == User.class)
        {
            return UserMapper.getInstance();
        }
        if(obj == PasswordToken.class)
        {
            return PasswordMapper.getInstance();
        }
        if(obj == Checkpoint.class){
            return checkpointMapper;
        }
        return null;
    }
}
