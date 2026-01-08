package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeverityResponseDto extends AuditDto {

    private Integer severityId;

    private String severityName;

    private Boolean isActive;
}
