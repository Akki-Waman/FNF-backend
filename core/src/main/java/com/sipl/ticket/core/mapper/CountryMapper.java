package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    /* ---------- CREATE ---------- */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "countryName", source = "countryName")
    @Mapping(target = "taxType", source = "taxType")
    @Mapping(target = "isForeign", source = "isForeign")
    @Mapping(target = "isActive", source = "isActive")
    Country toEntity(CountryRequestDto countryRequestDto);

    /* ---------- UPDATE ---------- */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "countryName", source = "countryName")
    @Mapping(target = "taxType", source = "taxType")
    @Mapping(target = "isForeign", source = "isForeign")
    @Mapping(target = "isActive", source = "isActive")
    void updateEntityFromDto(CountryRequestDto dto, @MappingTarget Country entity);

    /* ---------- RESPONSE ---------- */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "countryId", source = "countryId")
    @Mapping(target = "countryName", source = "countryName")
    @Mapping(target = "taxType", source = "taxType")
    @Mapping(target = "isForeign", source = "isForeign")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "createdTime", source = "createdTime")
    @Mapping(target = "modifiedTime", source = "modifiedTime")
    CountryResponseDto toDto(Country entity);
}
