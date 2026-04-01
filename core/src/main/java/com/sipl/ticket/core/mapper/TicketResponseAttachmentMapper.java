package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dao.entity.TicketResponseAttachment;
import com.sipl.ticket.core.dao.entity.TicketResponseCc;
import com.sipl.ticket.core.dto.response.TicketResponseAttachmentDTO;
import com.sipl.ticket.core.dto.response.TicketResponseCcDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TicketResponseMapper.class,
        }
)
public interface TicketResponseAttachmentMapper extends AuditEntityMapper {
    TicketResponseAttachment toEntity(TicketResponseAttachmentDTO dto);

    TicketResponseAttachmentDTO toDto(TicketResponse entity);

    List<TicketResponseAttachmentDTO> mapToDtoList(List<TicketResponseAttachment> entities);
}
