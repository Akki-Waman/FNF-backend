package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class CitySearchRequestDto extends SearchRequestDto {
    private Long cityId;
    private Long stateId;
    private String cityName;
}