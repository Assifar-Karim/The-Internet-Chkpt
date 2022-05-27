package com.ticp.mapper;

public interface Mapper<T, DTO>
{
    default DTO toDTO(T model)
    {
        return null;
    }
    default DTO toDTO(T model, Object object)
    {
        return null;
    }
    default T toModel(DTO dto)
    {
        return null;
    }
    default T toModel(DTO dto, T model)
    {
        return null;
    }
}
