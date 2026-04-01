package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.ReminderRecipient;
import com.sipl.ticket.core.dto.response.ReminderRecipientResponseDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class
        }
)
public interface ReminderRecipientMapper extends AuditEntityMapper {

    @Mapping(
            target = "channelTypeLabel",
            expression = "java(context != null ? context.resolveChannel(entity.getChannelType()) : null)"
    )
    @Mapping(target = "userId", source = "user.id")
    ReminderRecipientResponseDto toDto(
            ReminderRecipient entity,
            @Context MasterContext context
    );

    List<ReminderRecipientResponseDto> toDtoList(
            List<ReminderRecipient> entities,
            @Context MasterContext context
    );


    ReminderRecipient toEntity(ReminderRecipientResponseDto dto);

    List<ReminderRecipient> toEntityList(List<ReminderRecipientResponseDto> dtos);
}