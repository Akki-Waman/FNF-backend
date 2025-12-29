package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperatioalUnitDto extends AuditDto {
    private Long unitId;
    private String unitName;
    private DivisionResponseDTO division;
    private Boolean isActive;
}
