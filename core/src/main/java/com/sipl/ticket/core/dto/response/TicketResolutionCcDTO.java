package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResolutionCcDTO extends AuditDto {

    private Long ticketResolutionCcId;

    private TicketResolutionDTO ticketResolution;

    private String email;
}

