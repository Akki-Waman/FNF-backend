package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketTag;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.core.dto.response.TicketTagResponseDTO;
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
        uses = AuditUserMasterMapper.class
)
public interface TicketTagMapper extends AuditEntityMapper{

    @Mapping(target = "ticketTagId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    @Mapping(target = "tags", source = "tags")
    TicketTag toEntity(TicketTagResponseDTO dto, Ticket ticket, Tags tags);

    TicketTagResponseDTO toDto(TicketTag ticketTag);

    List<TicketTagResponseDTO> mapTagsListToDtoList(List<TicketTag> ticketTagList);

}
