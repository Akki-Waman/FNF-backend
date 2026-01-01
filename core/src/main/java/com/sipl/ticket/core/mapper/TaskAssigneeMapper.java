package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskAssignee;
import com.sipl.ticket.core.dto.response.TaskAssigneeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                TaskMapper.class,
                UserMapper.class,AuditUserMasterMapper.class
        }
)
public interface TaskAssigneeMapper extends AuditEntityMapper{

    TaskAssignee toEntity(TaskAssigneeDto dto);

    TaskAssigneeDto toDto(TaskAssignee entity);

    List<TaskAssigneeDto> mapToDtoList(List<TaskAssignee> entities);
}

