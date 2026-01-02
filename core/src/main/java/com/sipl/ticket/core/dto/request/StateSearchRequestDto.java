package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class StateSearchRequestDto extends SearchRequestDto {
    private Long stateId;
}
