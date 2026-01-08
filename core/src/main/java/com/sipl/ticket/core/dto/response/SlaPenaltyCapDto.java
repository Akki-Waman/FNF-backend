package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlaPenaltyCapDto extends AuditDto {

    private Integer slaPenaltyCapId;

    private SlaProfileResponseDto slaProfile;

    private Double maxPenaltyPercent;

    private String actionOnExceed;

    private Boolean isActive;
}
