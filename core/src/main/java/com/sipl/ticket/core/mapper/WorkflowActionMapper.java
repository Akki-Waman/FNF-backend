package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.WorkflowAction;
import com.sipl.ticket.core.dto.response.WorkflowActionDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface WorkflowActionMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toDto")
    WorkflowActionDTO toDto(WorkflowAction workflowAction);

    @InheritConfiguration(name = "toEntity")
    WorkflowAction toEntity(WorkflowActionDTO workflowActionDto);

    List<WorkflowActionDTO> toDtoList(List<WorkflowAction> workflowActions);

    List<WorkflowAction> toEntityList(List<WorkflowActionDTO> actionDtos);
}

