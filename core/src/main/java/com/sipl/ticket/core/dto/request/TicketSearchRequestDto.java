package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSearchRequestDto extends SearchRequestDto{
    private String query;
    private Integer ticketStatus;
    private List<Long> companyIds;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
