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

@Mapper(componentModel = "spring")
public interface FamilyMemberMapper {

    // 1. Converts the Create DTO
    FamilyMemberEntity toEntity(CreateFamilyMemberRequestDto dto);

    // 2. Converts the Add DTO (Fixes your missing method error)
    FamilyMemberEntity toEntity(AddFamilyMemberRequestDto dto);

    // 3. Converts Entity back to Response DTO
    FamilyMemberResponseDto toDto(FamilyMemberEntity entity);

    // 4. Converts Lists
    List<FamilyMemberResponseDto> toDtoList(List<FamilyMemberEntity> entities);

    // 5. Handles partial updates for existing records
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(AddFamilyMemberRequestDto dto, @MappingTarget FamilyMemberEntity entity);
}