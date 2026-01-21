package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dto.response.UnitDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface UnitMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Unit toEntity(UnitDto unitDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(source = "createdBy.userName", target = "createdBy")
    @Mapping(source = "modifiedBy.userName", target = "modifiedBy")
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "modifiedTime", target = "modifiedTime")
    UnitDto toDto(Unit unit);

    List<UnitDto> mapUnitListToDtoList(List<Unit> unitList);

    List<UnitDto> mapUnitDropListToDtoList(List<Unit> units);
}
