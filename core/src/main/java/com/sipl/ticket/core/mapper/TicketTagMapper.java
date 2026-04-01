package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketTag;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.core.dto.response.TicketTagResponseDTO;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {AuditUserMasterMapper.class,
                TicketMapper.class}
)
public interface TicketTagMapper extends AuditEntityMapper{

    @Mapping(target = "ticketTagId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    @Mapping(target = "tags", source = "tags")
    TicketTag toEntity(TicketTagResponseDTO dto, Ticket ticket, Tags tags);

    TicketTagResponseDTO toDto(TicketTag ticketTag);

    List<TicketTagResponseDTO> mapTagsListToDtoList(List<TicketTag> ticketTagList);

    List<TicketTagResponseDTO> toDtoList(List<TicketTag> byTicketId);
}
