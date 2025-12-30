package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneResponseDTO extends AuditDto{

    private Long zoneId;
    private String zoneName;
    private RegionResponseDTO region;
    private Boolean isActive = true;

}
