package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.AuditDto;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketNoteRequestDTO extends AuditDto {
    private Long ticketNoteId;
    private Long ticketId;
    private String notes;
    private Boolean isDeleted = false;
}
