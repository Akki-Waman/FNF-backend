package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Region;
import com.sipl.ticket.core.dto.response.RegionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface RegionMapper extends AuditEntityMapper{

    Region toEntity(RegionResponseDTO regionResponseDTO);

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    RegionResponseDTO toDto(Region region);

    List<RegionResponseDTO> mapRegionListToDtoList(List<Region> regionList);
}

