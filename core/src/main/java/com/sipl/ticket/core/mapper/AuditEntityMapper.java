package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.AuditEntity;
import com.sipl.ticket.core.dto.response.AuditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface AuditEntityMapper {
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    AuditDto toDto(AuditEntity entity);

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    AuditEntity toEntity(AuditDto dto);
}

