package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResolutionCombinedDto {

    private TicketResolutionDTO ticketResolutionDTO;
    private List<TicketResolutionCcDTO> ticketResolutionCcDTOS;
    private List<TicketResolutionAttachmentDTO> ticketResolutionAttachmentDTOS;
}
