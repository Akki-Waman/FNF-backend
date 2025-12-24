package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MovementFlowMapping;
import com.sipl.ticket.core.dto.response.MovementFlowMappingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementFlowMappingMapper {

    @Mapping(source = "movement.movementId", target = "movementId")
    @Mapping(source = "movement.movementDescription", target = "movementDescription")
    @Mapping(source = "processFlow.processFlowId", target = "processFlowId")
    @Mapping(source = "processFlow.processFlowName", target = "processFlowName")
    @Mapping(source = "plantMaster.plantId", target = "plantId")
    @Mapping(source = "plantMaster.plantCode", target = "plantCode")
    MovementFlowMappingResponseDTO toDto(MovementFlowMapping entity);

    @Mapping(target = "movement.movementId", source = "movementId")
    @Mapping(target = "processFlow.processFlowId", source = "processFlowId")
    @Mapping(target = "plantMaster.plantId", source = "plantId")
    MovementFlowMapping toEntity(MovementFlowMappingResponseDTO dto);
}
