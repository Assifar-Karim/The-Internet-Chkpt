package com.ticp.mapper;

import org.springframework.stereotype.Component;

@Component
public class ConcreteMapperFactory implements MapperFactory
{
    @Override
    public Mapper<?, ?> getMapper(String type)
    {
        if(type.equals("user"))
        {
            return UserMapper.getInstance();
        }
        if(type.equals("password"))
        {
            return PasswordMapper.getInstance();
        }
        return null;
    }
}
