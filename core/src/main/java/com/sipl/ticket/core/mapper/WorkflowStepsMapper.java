package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface WorkflowStepsMapper extends AuditEntityMapper {
    @InheritConfiguration(name = "toDto")
    @Mapping(target = "roleId", source = "role.userRoleId")
    @Mapping(target = "roleName", source = "role.userRoleName")
    WorkflowStepsDTO toDto(WorkflowSteps workflowSteps);

    @InheritConfiguration(name = "toEntity")
    WorkflowSteps toEntity(WorkflowStepsDTO workflowStepsDto);

    List<WorkflowStepsDTO> toDtoList(List<WorkflowSteps> workflowStepsList);
}

