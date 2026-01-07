package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.TicketCcResponseDTO;
import com.sipl.ticket.core.dto.response.TicketTagResponseDTO;
import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTicketsRequestDTO {
    private TicketsResponseDTO ticketsResponseDTO;
    private List<TicketTagResponseDTO> ticketTagResponseDTOS;
    private List<TicketCcResponseDTO> ticketCcResponseDTOS;
}
