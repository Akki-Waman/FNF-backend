package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Origins;
import com.sipl.ticket.core.dto.request.OriginsRequestDto;
import com.sipl.ticket.core.dto.response.OriginDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface OriginsMapper {


    @Mapping(target = "originId", ignore = true)
    Origins toEntity(OriginsRequestDto dto);


    OriginDto toDto(Origins origins);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "originId", ignore = true)
    void partialUpdate(OriginsRequestDto dto, @MappingTarget Origins origins);


    List<OriginDto> mapOriginsListToDtoList(List<Origins> originsList);

    List<OriginDto> mapOriginsDropListToDtoList(List<Origins> originsList);
}
