package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResolutionEmailRequestDto {
    private Long ticketResolutionId;

    private Long ticketResponseId;

    private TicketsResponseDTO ticket;

    private String responseBody;

    private String responseType;

    private Boolean isPublic;

    private String statusBefore;

    private String statusAfter;

    private Double  slaHours;

    private Boolean penaltyAllowed;

    private Double  responseTimeHours;

    private Boolean withinSla;

    private Integer penaltyTime;

    private BigDecimal penaltyPercentage;

    private List<String> ccMailIds;

    private List<String> emailIds;

}
