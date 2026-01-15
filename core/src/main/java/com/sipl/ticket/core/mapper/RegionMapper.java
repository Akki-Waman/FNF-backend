package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Region;
import com.sipl.ticket.core.dto.response.RegionResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface RegionMapper extends AuditEntityMapper{

    Region toEntity(RegionResponseDTO regionResponseDTO);

    RegionResponseDTO toDto(Region region);

    List<RegionResponseDTO> mapRegionListToDtoList(List<Region> regionList);
}

