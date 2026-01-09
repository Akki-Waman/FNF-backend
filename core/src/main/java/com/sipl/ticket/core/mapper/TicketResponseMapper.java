package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dto.response.TicketResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TicketMapper.class
        }
)
public interface TicketResponseMapper extends AuditEntityMapper {

    TicketResponse toEntity(TicketResponseDTO ticketResponseDTO);

    TicketResponseDTO toDto(TicketResponse ticketResponse);

    List<TicketResponseDTO> mapTicketResponseListToDtoList(List<TicketResponse> ticketResponseList);
}
