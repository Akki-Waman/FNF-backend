package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface MastersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Masters toEntity(MastersResponseDTO mastersResponseDTO);

    MastersResponseDTO toDto(Masters masters);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Masters partialUpdate(MastersResponseDTO mastersResponseDTO, @MappingTarget Masters masters);

    List<MastersResponseDTO> toDtoList(List<Masters> mastersList);

}
