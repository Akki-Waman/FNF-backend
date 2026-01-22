package com.sipl.ticket.core.dto.request;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusRequestDTO {
    private Long ticketId;
    private Integer status;
}
