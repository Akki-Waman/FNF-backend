package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.TicketResolutionAttachment;
import com.sipl.ticket.core.dto.response.TicketResolutionAttachmentDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                TicketResolutionMapper.class
        }
)
public interface TicketResolutionAttachmentMapper extends AuditEntityMapper {

    TicketResolutionAttachment toEntity(TicketResolutionAttachmentDTO dto);

    TicketResolutionAttachmentDTO toDto(TicketResolutionAttachment entity);

    List<TicketResolutionAttachmentDTO> mapToDtoList(
            List<TicketResolutionAttachment> entities
    );
}
