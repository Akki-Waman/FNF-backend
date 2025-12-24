package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TransactionStages;
import com.sipl.ticket.core.dto.response.TransactionStageResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AuditUserMasterMapper.class})
public interface TransactionStageMapper  {

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "location", ignore = true)
    @Mapping(target = "masterScreen", ignore = true)
    TransactionStages toEntity(TransactionStageResponseDTO dto);

//    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "movementFlowMappingId", source = "movementFlowMapping.movementFlowMappingId")
    @Mapping(target = "plantCode", source = "plant.plantCode")
    @Mapping(target = "actionBy", source = "actionBy.userName")
//    @Mapping(target = "location", source = "location.locationName")
    @Mapping(target = "masterId", source = "masterScreen.masterId")
    @Mapping(target = "masterName", source = "masterScreen.masterName")
    @Mapping(target = "processFlowName", source = "movementFlowMapping.processFlow.processFlowName")
    TransactionStageResponseDTO toDto(TransactionStages entity);

    List<TransactionStageResponseDTO> toDtoList(List<TransactionStages> entityList);
}
