package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketNote;
import com.sipl.ticket.core.dto.request.TicketEmailRequestDto;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.core.dto.response.TicketCombinedResponseDto;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                ContactMapper.class,
                LocationMapper.class,
                DepartmentMapper.class,
                ClientProductMapper.class,
                ServiceMapper.class,
                BranchMapper.class,
                ShiftMapper.class
        }
)
public interface TicketMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Ticket toEntity(TicketsResponseDTO ticketsResponseDTO);

    @Mapping(
            target = "createdByUsername",
            expression = "java(ticket.getCreatedBy() != null ? ticket.getCreatedBy().getUserName() : \"\")"
    )
    @Mapping(
            target = "firstNote",
            expression = "java(getFirstNote(ticket))"
    )
    @Mapping(
            target = "locationName",
            expression = "java(ticket.getLocation() != null ? ticket.getLocation().getLocationName() : \"\")"
    )
    @Mapping(
            target = "priorityLabel",
            expression = "java(context.resolvePriority(ticket.getPriority()))"
    )
    @Mapping(
            target = "statusLabel",
            expression = "java(context.resolveStatus(ticket.getStatus()))"
    )
    @Mapping(
            target = "tags",
            expression = "java(mapTags(ticket))"
    )
    @Mapping(
            target = "startDateTime",
            expression = "java(ticket.getResponseDateTime() != null ? ticket.getResponseDateTime() : ticket.getCreatedTime())"
    )
    @Mapping(
            target = "endDateTime",
            expression = "java(ticket.getResolutionDateTime())"
    )
    TicketsResponseDTO toTicketDto(
            Ticket ticket,
            @Context MasterContext context
    );


    List<TicketsResponseDTO> toTicketDtoList(
            List<Ticket> tickets,
            @Context MasterContext context
    );

    TicketCombinedResponseDto toCombinedDto(Ticket ticket);

    default String getFirstNote(Ticket ticket) {
        if (ticket.getNotes() == null) return "";

        return ticket.getNotes().stream()
                .filter(n -> Boolean.FALSE.equals(n.getIsDeleted()))
                .sorted(Comparator.comparing(TicketNote::getCreatedTime))
                .map(TicketNote::getNotes)
                .findFirst()
                .orElse("");
    }


    default String mapTags(Ticket ticket) {
        if (ticket.getTicketTags() == null) return "";

        return ticket.getTicketTags().stream()
                .map(t -> t.getTags() != null ? t.getTags().getTagName() : "")
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
    }

    TicketCombinedResponseDto toCombinedResponseDto(
            Ticket ticket,
            @Context MasterContext masterContext
    );


    TicketEmailRequestDto toTicketEmailRequestDto(Ticket ticket);
}







