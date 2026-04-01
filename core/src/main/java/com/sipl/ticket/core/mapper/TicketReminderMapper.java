package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.TicketReminder;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                ReminderRecipientMapper.class
        }
)
public interface TicketReminderMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toDto")
    @Mapping(
            target = "statusLabel",
            expression = "java(context != null ? context.resolveStatus(entity.getStatus()) : null)"
    )
    @Mapping(
            target = "recurrenceIntervalLabel",
            expression = "java(entity.getRecurrenceInterval() != null && context != null ? context.resolveRecurrence(entity.getRecurrenceInterval()) : null)"
    )
    @Mapping(
            target = "ticketId",
            source = "ticket.ticketId"
    )
    ReminderResponseDto toDto(
            TicketReminder entity,
            @Context MasterContext context
    );

    List<ReminderResponseDto> toDtoList(
            List<TicketReminder> entities,
            @Context MasterContext context
    );


    @InheritConfiguration(name = "toEntity")
    TicketReminder toEntity(ReminderResponseDto dto);

    List<TicketReminder> toEntityList(List<ReminderResponseDto> dtos);
}