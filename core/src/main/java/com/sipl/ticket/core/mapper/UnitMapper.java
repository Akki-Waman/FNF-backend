package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dto.response.UnitDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface UnitMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Unit toEntity(UnitDto unitDto);

    @InheritConfiguration(name = "toDto")
    UnitDto toDto(Unit unit);

    List<UnitDto> mapUnitListToDtoList(List<Unit> unitList);

    List<UnitDto> mapUnitDropListToDtoList(List<Unit> units);
}
