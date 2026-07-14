package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dto.requestDto.CreateProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import com.ensf.fnf.core.dto.responseDto.UserProfilesResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserEntity toEntity(CreateProfileRequestDto dto);

    UserProfileResponseDto toDto(UserEntity entity);

    List<UserProfileResponseDto> toDtoList(
            List<UserEntity> entities
    );

    void partialUpdate(
            CreateProfileRequestDto dto,
            @MappingTarget UserEntity entity
    );

    @Mapping(
            source = "emailVerified",
            target = "verifiedEmail"
    )
    UserProfilesResponseDto toDtos(UserEntity entity);
}