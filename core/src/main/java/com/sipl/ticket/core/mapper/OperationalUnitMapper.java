package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.OperationalUnit;
import com.sipl.ticket.core.dao.entity.Region;
import com.sipl.ticket.core.dto.response.OperationalUnitResponseDTO;
import com.sipl.ticket.core.dto.response.RegionResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                DivisionMapper.class
        }
)
public interface OperationalUnitMapper extends AuditEntityMapper{
    OperationalUnit toEntity(OperationalUnitResponseDTO operationalUnitResponseDTO);

    OperationalUnitResponseDTO toDto(OperationalUnit operationalUnit);

    List<OperationalUnitResponseDTO> mapOpUnitListToDtoList(List<OperationalUnit> operationalUnits);
}
