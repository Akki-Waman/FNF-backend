package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dto.request.TicketResponseRequestDTO;
import com.sipl.ticket.core.dto.response.TicketResponseDTO;
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
public interface TicketResponseMapper extends AuditEntityMapper {

    @Mapping(target = "ticket", ignore = true)
    @Mapping(
            target = "statusAfter",
            expression = "java(masterContext.resolveStatus(dto.getStatus()))"
    )
    TicketResponse toEntity(TicketResponseRequestDTO dto,
                            @Context MasterContext masterContext);


    TicketResponseDTO toDto(TicketResponse ticketResponse);

    List<TicketResponseDTO> mapTicketResponseListToDtoList(List<TicketResponse> ticketResponseList);
}
