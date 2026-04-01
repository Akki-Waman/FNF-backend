package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketCc;
import com.sipl.ticket.core.dao.entity.TicketNote;
import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.TicketCcResponseDTO;
import com.sipl.ticket.core.dto.response.TicketNoteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {AuditUserMasterMapper.class,
                TicketMapper.class}
)
public interface TicketNoteMapper extends AuditEntityMapper{
    @Mapping(target = "ticket", source = "ticket")
    TicketNote toEntity(TicketNoteResponseDTO dto);

    TicketNoteResponseDTO toDto(TicketNote ticketNote);

    List<TicketNoteResponseDTO> mapTicketNoteListToDtoList(List<TicketNote> ticketNotes);
}
