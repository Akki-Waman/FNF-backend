package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseCombinedDto {
    private TicketResponseDTO ticketResponseDTO;
    private List<TicketResponseCcDTO> ticketResponseCcDTOS;
    private List<TicketResponseAttachmentDTO> ticketResponseAttachmentDTOS;
}
