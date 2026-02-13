package com.sipl.ticket.core.dto.response;

import lombok.Data;


@Data
public class TicketResolutionDTO extends AuditDto {

    private Long ticketResolutionId;

    private TicketsResponseDTO ticket;

    private String resolutionBody;

    private Boolean isPublic;

    private String statusBefore;

    private String statusAfter;
}
