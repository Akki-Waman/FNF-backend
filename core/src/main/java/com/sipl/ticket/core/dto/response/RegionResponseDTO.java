package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionResponseDTO extends AuditDto{

    private Long regionId;

    private String regionName;

    private Boolean isActive = true;
}
