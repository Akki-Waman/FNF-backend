package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dto.requestDto.CreateAccountRequestDto;
import com.ensf.fnf.core.dto.responseDto.UserResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserEntity toEntity(
            CreateAccountRequestDto dto
    );

    UserResponseDto toDto(
            UserEntity entity
    );
}