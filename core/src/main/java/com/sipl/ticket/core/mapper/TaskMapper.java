package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dto.response.TaskCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                BranchMapper.class,
                TicketMapper.class
        }
)
public interface TaskMapper extends AuditEntityMapper{

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);

    List<TaskDto> mapTaskListToDtoList(List<Task> taskList);

    TaskCustomResponseDTO toCustomDto(Task task);
}
