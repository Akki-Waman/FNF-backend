package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Movements;
import com.sipl.ticket.core.dto.request.MovementsRequestDTO;
import com.sipl.ticket.core.dto.response.MovementsResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementsMapper {

    MovementsResponseDTO toDto(Movements movements);

    List<MovementsResponseDTO> toDtoList(List<Movements> movementsList);

    @Mapping(target = "movementId", ignore = true)
    Movements toEntity(MovementsRequestDTO dto);

    @Mapping(target = "movementId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Movements partialUpdate(MovementsRequestDTO dto, @MappingTarget Movements movements);
}
