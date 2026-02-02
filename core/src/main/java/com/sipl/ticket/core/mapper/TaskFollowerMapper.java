package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskFollower;
import com.sipl.ticket.core.dto.response.TaskFollowerCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskFollowerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TaskMapper.class,
                UserMapper.class
        }
)
public interface TaskFollowerMapper extends AuditEntityMapper{

    TaskFollower toEntity(TaskFollowerDto dto);

    TaskFollowerDto toDto(TaskFollower entity);

    List<TaskFollowerDto> mapToDtoList(List<TaskFollower> entities);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "task.subject", target = "subject")
    TaskFollowerCustomResponseDTO toCustomDto(TaskFollower taskFollower);
}
