package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitySearchResponseDto extends AuditDto {

    private Long cityId;
    private String cityName;

    private Long stateId;
    private String stateName;

    private Long countryId;
    private String countryName;

    private Boolean isActive;
}
