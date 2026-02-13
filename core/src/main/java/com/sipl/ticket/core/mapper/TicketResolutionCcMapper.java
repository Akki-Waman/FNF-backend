package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TicketResolutionCc;
import com.sipl.ticket.core.dto.response.TicketResolutionCcDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TicketResolutionMapper.class
        }
)
public interface TicketResolutionCcMapper extends AuditEntityMapper {

    TicketResolutionCc toEntity(TicketResolutionCcDTO dto);

    TicketResolutionCcDTO toDto(TicketResolutionCc entity);

    List<TicketResolutionCcDTO> mapToDtoList(
            List<TicketResolutionCc> entities
    );
}
