package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCcResponseDTO {

    private Long ticketCcId;

    private TicketsResponseDTO ticket;

    private String cc;
}
