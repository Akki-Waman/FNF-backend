package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketNoteResponseDTO extends AuditDto{
    private Long ticketNoteId;
    private TicketsResponseDTO ticket;
    private String notes;
    private Boolean isDeleted = false;
}
