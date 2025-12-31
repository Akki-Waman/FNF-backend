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
        uses = AuditUserMasterMapper.class
)
public interface TicketAttachmentMapper {
    @Mapping(target = "attachmentId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    @Mapping(target = "uploadedBy", source = "uploadedBy")// use @Context Ticket
    TicketAttachment toEntity(TicketAttachmentResponseDTO dto, Ticket ticket,  Users uploadedBy);

    @InheritConfiguration(name = "toDto")
    @Mapping(target = "ticket.assignedTo.createdTime", source = "ticket.assignedTo.createdTime")
    TicketAttachmentResponseDTO toDto(TicketAttachment ticketAttachment);

    List<TicketAttachmentResponseDTO> mapTagsListToDtoList(List<TicketAttachment> ticketAttachmentList);

}
