package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.TicketAttachmentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombinedTicketResponseDto {
    private TicketsResponseDTO ticketsResponseDTO;
    private List<TicketTagResponseDTO> ticketTagResponseDTOS;
    private List<TicketCcResponseDTO> ticketCcResponseDTOS;
    private List<TicketAttachmentResponseDTO> attachments;

}
