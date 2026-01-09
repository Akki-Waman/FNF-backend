package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class StateSearchRequestDto extends SearchRequestDto {
    private String query;
}
