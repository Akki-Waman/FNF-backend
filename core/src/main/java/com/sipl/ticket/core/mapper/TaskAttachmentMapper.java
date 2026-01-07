package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskAttachment;
import com.sipl.ticket.core.dto.request.TaskAttachmentDto;
import com.sipl.ticket.core.dto.response.TaskAttachmentCustomResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses =AuditUserMasterMapper.class)
public interface TaskAttachmentMapper extends AuditEntityMapper{

    TaskAttachment toEntity(TaskAttachmentDto dto);

    TaskAttachmentDto toDto(TaskAttachment entity);

    List<TaskAttachmentDto> mapToDtoList(List<TaskAttachment> entities);

    TaskAttachmentCustomResponseDTO toCustomDto(TaskAttachment taskAttachment);
}

