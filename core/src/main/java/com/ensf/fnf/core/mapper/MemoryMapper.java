package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.MemoryEntity;
import com.ensf.fnf.core.dto.responseDto.MemoryResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemoryMapper {

    // Converts a single entity to a response DTO
    MemoryResponseDto toDto(MemoryEntity entity);

    // Converts a list of entities to a list of response DTOs
    List<MemoryResponseDto> toDtoList(List<MemoryEntity> entities);
}