package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlaTypeDto extends AuditDto {

    private Integer slaTypeId;

    private String slaTypeName;

    private Boolean isActive;
}
