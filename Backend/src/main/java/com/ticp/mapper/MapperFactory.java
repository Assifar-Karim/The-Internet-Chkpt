package com.ticp.mapper;

public interface MapperFactory
{
    Mapper<?, ?> getMapper( Class <?> obj );
}
