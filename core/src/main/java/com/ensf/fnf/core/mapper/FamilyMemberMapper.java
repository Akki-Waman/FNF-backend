package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dto.requestDto.AddFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface FamilyMemberMapper {

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
            AddFamilyMemberRequestDto dto,
            @MappingTarget FamilyMemberEntity entity
    );
}