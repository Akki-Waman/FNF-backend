package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface FamilyMemberMapper {

    @Mappings({
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "relationshipType", target = "relationshipType"),
            @Mapping(source = "birthDate", target = "birthDate"),
            @Mapping(source = "married", target = "married"),
            @Mapping(source = "anniversaryDate", target = "anniversaryDate"),
            @Mapping(source = "spouseName", target = "spouseName"),
            @Mapping(source = "spouseBirthDate", target = "spouseBirthDate")
    })
    FamilyMemberEntity toEntity(
            CreateFamilyMemberRequestDto dto
    );

    FamilyMemberResponseDto toDto(
            FamilyMemberEntity entity
    );

    List<FamilyMemberResponseDto> toDtoList(
            List<FamilyMemberEntity> entities
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void partialUpdate(
            CreateFamilyMemberRequestDto dto,
            @MappingTarget FamilyMemberEntity entity
    );
}