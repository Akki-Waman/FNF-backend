package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.UnitDto;
import org.mapstruct.*;

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

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userToName")
    @Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = "userToName")
    UnitDto toDto(Unit unit);

    /* ================= UPDATE ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "unitId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    void partialUpdate(UnitRequestDto dto, @MappingTarget Unit unit);

    /* ================= USER → STRING ================= */

    @Named("userToName")
    default String mapUser(Users user) {
        return user == null ? null : user.getUserName();
    }
}
