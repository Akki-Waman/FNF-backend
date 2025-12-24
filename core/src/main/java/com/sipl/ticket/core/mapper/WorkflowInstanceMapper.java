package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.WorkflowInstance;
import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface WorkflowInstanceMapper extends AuditEntityMapper {

    @Mapping(target = "actionBy", ignore = true)
    WorkflowInstanceDTO toDto(WorkflowInstance workflowInstance);


    WorkflowInstance toEntity(WorkflowInstanceDTO workflowInstanceDto);

    List<WorkflowInstanceDTO> toDtoList(List<WorkflowInstance> workflowInstances);

    List<WorkflowInstance> toEntityList(List<WorkflowInstanceDTO> instanceDtos);
}
