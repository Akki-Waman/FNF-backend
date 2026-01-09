package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseRequestDTO {
    private Long ticketResponseId;
    private Long ticket;
    private String responseBody;
    private String responseType;
    private Boolean isPublic;
    private String statusBefore;
    private String statusAfter;
    private List<String> ccEmails;
}
