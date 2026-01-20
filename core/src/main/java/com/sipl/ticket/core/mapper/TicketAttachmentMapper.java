package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {AuditUserMasterMapper.class,
        TicketMapper.class}
)
public interface TicketAttachmentMapper {
    @Mapping(target = "attachmentId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    TicketAttachment toEntity(TicketAttachmentResponseDTO dto, Ticket ticket,  Users uploadedBy);

    @InheritConfiguration(name = "toDto")
    @Mapping(source = "ticket.ticketId", target = "ticket")
    TicketAttachmentResponseDTO toDto(TicketAttachment ticketAttachment);

    List<TicketAttachmentResponseDTO> mapTagsListToDtoList(List<TicketAttachment> ticketAttachmentList);

    @Mapping(source = "ticket.ticketId", target = "ticketId")
    List<TicketAttachmentResponseDTO> toDtoList(List<TicketAttachment> byTicketId);
}
