package com.deliverytech.delivery_api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoMapper {
    
    private final ModelMapper modelMapper;
    
    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    public <T> T toDto(Object source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
    
    public <T> T toEntity(Object source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
    
    public <T> List<T> toDtoList(List<?> source, Class<T> targetClass) {
        return source.stream()
            .map(item -> toDto(item, targetClass))
            .collect(Collectors.toList());
    }
    
    public <T> List<T> toEntityList(List<?> source, Class<T> targetClass) {
        return source.stream()
            .map(item -> toEntity(item, targetClass))
            .collect(Collectors.toList());
    }
}