package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePenaltyRequestDTO extends SearchRequestDto{
    private Long ticketId;
    private String unitName;
    private String deviceName;
    private String service;
}
