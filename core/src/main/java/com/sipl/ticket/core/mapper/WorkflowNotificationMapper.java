package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.WorkflowNotification;
import com.sipl.ticket.core.dto.response.WorkflowNotificationDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface WorkflowNotificationMapper extends AuditEntityMapper {
    @InheritConfiguration(name = "toDto")
    WorkflowNotificationDTO toDto(WorkflowNotification workflowNotification);

    @InheritConfiguration(name = "toEntity")
    WorkflowNotification toEntity(WorkflowNotificationDTO workflowNotificationDto);
}

