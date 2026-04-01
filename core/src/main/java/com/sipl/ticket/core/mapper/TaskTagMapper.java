package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskTag;
import com.sipl.ticket.core.dto.response.TaskTagCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskTagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TaskMapper.class,
                TagsMapper.class
        }
)
public interface TaskTagMapper extends AuditEntityMapper{

    TaskTag toEntity(TaskTagDto dto);

    TaskTagDto toDto(TaskTag entity);

    List<TaskTagDto> mapToDtoList(List<TaskTag> entities);

    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "task.subject", target = "subject")
    @Mapping(source = "taskTagId", target = "taskTagId")
    @Mapping(source = "tag.tagId", target = "tagId")
    @Mapping(source = "tag.tagName", target = "tagName")
    TaskTagCustomResponseDTO toCustomDto(TaskTag entity);
}
