package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface TagsMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "branch", ignore = true)
    Tags toEntity(TagResponseDto tagResponseDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "branchName", source = "branch.branchName")
    TagResponseDto toDto(Tags tags);

    List<TagResponseDto> mapTagsListToDtoList(List<Tags> tagsList);
}
