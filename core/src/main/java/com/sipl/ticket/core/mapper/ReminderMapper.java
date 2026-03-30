package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ReminderRecipient;
import com.sipl.ticket.core.dao.entity.TicketReminder;
import com.sipl.ticket.core.dto.response.ReminderRecipientResponseDto;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    @Mapping(source = "status.columnName", target = "status")
    ReminderResponseDto toDto(TicketReminder entity);

    List<ReminderResponseDto> toDtoList(List<TicketReminder> entities);


    @Mapping(source = "channelType.columnName", target = "channelType")
    @Mapping(source = "status.columnName", target = "status")
    ReminderRecipientResponseDto toRecipientDto(ReminderRecipient entity);

    List<ReminderRecipientResponseDto> toRecipientDtoList(List<ReminderRecipient> entities);
}