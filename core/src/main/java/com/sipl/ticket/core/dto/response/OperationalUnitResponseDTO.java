package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationalUnitResponseDTO extends AuditDto{

    private Long operationalUnitId;

    private String operationalUnitName;

    private DivisionResponseDTO division;

    private Boolean isActive = true;
}
