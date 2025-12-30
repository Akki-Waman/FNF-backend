package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface TagsMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Tags toEntity(TagResponseDto tagResponseDto);

    @InheritConfiguration(name = "toDto")
    TagResponseDto toDto(Tags tags);

    List<TagResponseDto> mapTagsListToDtoList(List<Tags> tagsList);
}
