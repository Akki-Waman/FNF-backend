package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlaRuleDetailsDto extends AuditDto {

    private Integer slaRuleDetailId;

    private SlaProfileResponseDto slaProfile;

    private ServiceResponseDTO service;

    private SeverityResponseDto severity;

    private SlaTypeDto slaType;

    private Integer slaHours;

    private Integer graceHours;

    private Double penaltyPercent;

    private Boolean isActive;
}
