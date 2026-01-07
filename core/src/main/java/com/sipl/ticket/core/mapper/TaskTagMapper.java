package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TaskTag;
import com.sipl.ticket.core.dto.response.TaskTagCustomResponseDTO;
import com.sipl.ticket.core.dto.response.TaskTagDto;
import org.mapstruct.Mapper;

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

    TaskTagCustomResponseDTO toCustomDto(Object o);
}
