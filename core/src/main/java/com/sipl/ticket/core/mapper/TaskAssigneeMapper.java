package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskAssignee;
import com.sipl.ticket.core.dto.response.TaskAssigneeCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskAssigneeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    Object toCustomDto(Object o);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "task.subject", target = "subject")
    TaskAssigneeCustomResponseDTO toCustomDto(TaskAssignee taskAssignee);
}

