package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.TaskTag;
import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dao.entity.TicketResponseCc;
import com.sipl.ticket.core.dto.response.TaskTagDto;
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
public interface TicketResponseCcMapper extends AuditEntityMapper {
    TicketResponseCc toEntity(TicketResponseCcDTO dto);

    TicketResponseCcDTO toDto(TicketResponse entity);

    List<TicketResponseCcDTO> mapToDtoList(List<TicketResponseCc> entities);
}
