package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.City;
import com.sipl.ticket.core.dto.response.CityResponseDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface CityMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    City toEntity(CityResponseDto cityResponseDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(source = "state.stateId", target = "stateId")
    @Mapping(source = "state.stateName", target = "stateName")
    CityResponseDto toDto(City city);

    List<CityResponseDto> toDtoList(List<City> cities);
}