package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.State;
import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.response.StateResponseDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class
        }
)
public interface StateMapper extends AuditEntityMapper {


    @InheritConfiguration(name = "toEntity")
    @Mapping(source = "countryId", target = "country.countryId")
    State toEntity(StateRequestDto stateRequestDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(source = "country.countryId", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryName")
    StateResponseDto toDto(State state);

    List<StateResponseDto> mapStateListToDtoList(List<State> states);

    List<StateResponseDto> mapStateDropListToDtoList(List<State> states);
}

