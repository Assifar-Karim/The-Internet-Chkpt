package com.ticp.mapper;

public interface MapperFactory
{
    Mapper<?, ?> getMapper(String type);
}
