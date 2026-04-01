package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResolutionRequestDTO {

    private Long ticketResolutionId;

    private Long ticket;

    private String resolutionBody;
    
    private Boolean isPublic;

    private String statusBefore;

    private String statusAfter;

    private Integer status;

    private List<String> ccEmails;
}
