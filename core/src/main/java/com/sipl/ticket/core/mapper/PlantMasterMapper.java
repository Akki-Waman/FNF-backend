package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.PlantMaster;
import com.sipl.ticket.core.dto.request.PlantMasterRequestDTO;
import com.sipl.ticket.core.dto.response.PlantMasterResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring",
        uses = {AuditUserMasterMapper.class})
public interface PlantMasterMapper {
    PlantMaster toEntity(PlantMasterRequestDTO dto);

    PlantMasterResponseDTO toDto(PlantMaster entity);

    List<PlantMasterResponseDTO> toDtoList(List<PlantMaster> entityList);

    List<PlantMaster> toEntityList(List<PlantMasterRequestDTO> dtoList);
}
