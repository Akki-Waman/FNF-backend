package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Divisions;
import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dto.response.DivisionResponseDTO;
import com.sipl.ticket.core.dto.response.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                ZoneMapper.class
        }
)
public interface DivisionMapper extends AuditEntityMapper{
    Divisions toEntity(DivisionResponseDTO divisionResponseDTO);

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    DivisionResponseDTO toDto(Divisions divisions);

    List<DivisionResponseDTO> mapDivisionListToDtoList(List<Divisions> divisionsList);
}
