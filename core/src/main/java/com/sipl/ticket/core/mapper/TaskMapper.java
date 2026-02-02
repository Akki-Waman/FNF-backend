package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dto.response.TaskCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskDto;
import com.sipl.ticket.core.dto.response.TaskExportDTO;
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

    @Mapping(source = "branch.branchId", target = "branchId")
    @Mapping(source = "branch.branchName", target = "branchName")
    @Mapping(source = "ticket.ticketId", target = "ticketId")
    @Mapping(source = "ticket.subject", target = "ticketSubject")
    TaskCustomResponseDTO toCustomDto(Task task);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "taskId", source = "taskId")
    @Mapping(target = "taskName", source = "subject")
    @Mapping(
            target = "status",
            expression = "java(context.resolveStatus(task.getStatus()))"
    )
    @Mapping(
            target = "priority",
            expression = "java(context.resolvePriority(task.getPriority()))"
    )
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "dueDate", source = "dueDate")
    TaskExportDTO toExportDto(
            Task task,
            @Context MasterContext context
    );
}
