package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                ContactMapper.class,
                LocationMapper.class,
                DepartmentMapper.class,
                ClientProductMapper.class,
                ServiceMapper.class,
                BranchMapper.class
        }
)
public interface TicketMapper extends AuditEntityMapper {
    @InheritConfiguration(name = "toEntity")
    Ticket toEntity(TicketsResponseDTO ticketsResponseDTO);

    @InheritConfiguration(name = "toDto")
    TicketsResponseDTO toDto(Ticket ticket);

    List<TicketsResponseDTO> mapTagsListToDtoList(List<Ticket> tickets);


}


