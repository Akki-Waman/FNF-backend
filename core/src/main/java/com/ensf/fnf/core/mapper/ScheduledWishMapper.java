package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.ScheduledWishEntity;
import com.ensf.fnf.core.dto.responseDto.ScheduledWishResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduledWishMapper {

    ScheduledWishResponseDto toDto(ScheduledWishEntity entity);

    List<ScheduledWishResponseDto> toDtoList(List<ScheduledWishEntity> entities);
}