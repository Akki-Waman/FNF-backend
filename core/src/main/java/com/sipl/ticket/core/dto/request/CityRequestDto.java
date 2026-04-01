package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDto {
    private Long cityId;
    private String cityName;
    private Long stateId;
    private Boolean isActive;

}