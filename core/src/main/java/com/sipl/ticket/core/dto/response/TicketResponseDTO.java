package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO extends AuditDto{

    private Long ticketResponseId;

    private TicketsResponseDTO ticket;

    private String responseBody;

    private String responseType;

    private Boolean isPublic;

    private String statusBefore;

    private String statusAfter;
}
