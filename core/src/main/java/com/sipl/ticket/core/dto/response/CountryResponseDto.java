package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDto extends AuditDto {

    private Long countryId;
    private String countryName;
    private String taxType;
    private Boolean isForeign;
    private Boolean isActive;
}
