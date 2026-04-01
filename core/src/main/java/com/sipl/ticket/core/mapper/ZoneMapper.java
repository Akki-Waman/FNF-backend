package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dao.entity.Zone;
import com.sipl.ticket.core.dto.response.TaskDto;
import com.sipl.ticket.core.dto.response.ZoneResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                RegionMapper.class
        }
)
public interface ZoneMapper extends AuditEntityMapper {
    Zone toEntity(ZoneResponseDTO zoneResponseDTO);

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    ZoneResponseDTO toDto(Zone zone);

    List<ZoneResponseDTO> mapZoneListToDtoList(List<Zone> zoneList);
}
