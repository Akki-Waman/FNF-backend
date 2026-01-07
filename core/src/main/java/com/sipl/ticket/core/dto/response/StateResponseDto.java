package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDto extends AuditDto {

    private Long stateId;
    private String stateName;

    private Long countryId;
    private String countryName;

    private Boolean isActive;
}
