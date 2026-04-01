package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


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

    private Integer slaHours;

    private Boolean penaltyAllowed;

    private Long responseTimeHours;

    private Boolean withinSla;

    private Integer penaltyTime;

    private BigDecimal penaltyPercentage;
}
