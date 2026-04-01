package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Locations;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface LocationMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "branch", ignore = true)
    Locations toEntity(LocationResponseDTO locationResponseDTO);

    @InheritConfiguration(name = "toDto")
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "branchName", source = "branch.branchName")
    LocationResponseDTO toDto(Locations locations);

    List<LocationResponseDTO> mapLocationsListToDtoList(List<Locations> locationsList);

    List<LocationResponseDTO> mapLocationsDropListToDtoList(List<Locations> locations);

}
