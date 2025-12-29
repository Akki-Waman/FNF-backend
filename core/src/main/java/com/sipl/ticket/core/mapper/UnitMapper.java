package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.AuditDto;
import com.sipl.ticket.core.dto.response.UnitResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    /* ================= CREATE ================= */

    @Mapping(target = "unitId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)
    Unit toEntity(UnitRequestDto dto);

    /* ================= RESPONSE ================= */

    @Mapping(
            target = "auditDto",
            expression = "java(mapAudit(unit))"
    )
    UnitResponseDto toResponseDto(Unit unit);

    /* ================= UPDATE ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "unitId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    void partialUpdate(
            UnitRequestDto dto,
            @MappingTarget Unit unit
    );

    /* ================= LIST ================= */

    List<UnitResponseDto> toResponseDtoList(List<Unit> units);

    /* ================= AUDIT MAPPING ================= */

    default AuditDto mapAudit(Unit unit) {
        if (unit == null) {
            return null;
        }
        return new AuditDto(
                unit.getCreatedBy() != null ? unit.getCreatedBy().getUsername() : null,
                unit.getModifiedBy() != null ? unit.getModifiedBy().getUsername() : null,
                unit.getCreatedTime(),
                unit.getModifiedTime()
        );
    }

}
