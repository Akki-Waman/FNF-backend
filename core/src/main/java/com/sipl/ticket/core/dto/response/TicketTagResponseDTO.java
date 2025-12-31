package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTagResponseDTO {

    private Long ticketTagId;

    private TicketsResponseDTO tickets;

    private TagResponseDto tags;
}

