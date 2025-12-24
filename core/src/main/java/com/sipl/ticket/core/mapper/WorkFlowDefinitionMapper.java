package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.WorkFlowDefinition;
import com.sipl.ticket.core.dto.response.WorkFlowDefinitionDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface WorkFlowDefinitionMapper extends AuditEntityMapper{

    @InheritConfiguration(name = "toDto")
    WorkFlowDefinitionDTO toDto(WorkFlowDefinition workFlowDefinition);

    @InheritConfiguration(name = "toEntity")
    WorkFlowDefinition toEntity(WorkFlowDefinitionDTO workFlowDefinitionDto);

    List<WorkFlowDefinitionDTO> toDtoList(List<WorkFlowDefinition> workFlowDefinitionList);
}
