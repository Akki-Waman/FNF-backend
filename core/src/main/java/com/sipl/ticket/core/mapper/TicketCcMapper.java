package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketCc;
import com.sipl.ticket.core.dto.response.TicketCcResponseDTO;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {AuditUserMasterMapper.class,
                TicketMapper.class}
)
public interface TicketCcMapper {
    @Mapping(target = "ticketCcId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    TicketCc toEntity(TicketCcResponseDTO dto, Ticket ticket);

    TicketCcResponseDTO toDto(TicketCc ticketCc);

    List<TicketCcResponseDTO> mapTagsListToDtoList(List<TicketCc> ticketCcList);

    List<TicketCcResponseDTO> toDtoList(List<TicketCc> byTicketId);
}