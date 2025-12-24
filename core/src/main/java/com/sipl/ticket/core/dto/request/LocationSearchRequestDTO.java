package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationSearchRequestDTO extends SearchRequestDto{
    private String search;
}
