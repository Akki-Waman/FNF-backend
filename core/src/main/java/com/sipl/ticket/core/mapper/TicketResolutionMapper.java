package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.TicketResolution;
import com.sipl.ticket.core.dto.request.TicketResolutionRequestDTO;
import com.sipl.ticket.core.dto.response.TicketResolutionDTO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TicketMapper.class
        }
)
public interface TicketResolutionMapper extends AuditEntityMapper {

    @Mapping(target = "ticket", ignore = true)
    @Mapping(
            target = "statusAfter",
            expression = "java(masterContext.resolveStatus(dto.getStatus()))"
    )
    TicketResolution toEntity(TicketResolutionRequestDTO dto,
                              @Context MasterContext masterContext);

    TicketResolutionDTO toDto(TicketResolution ticketResolution);

    List<TicketResolutionDTO> mapTicketResolutionListToDtoList(
            List<TicketResolution> ticketResolutionList
    );
}
