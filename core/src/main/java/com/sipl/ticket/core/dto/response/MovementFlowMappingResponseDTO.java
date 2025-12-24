package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementFlowMappingResponseDTO {

    private Long movementFlowMappingId;
    private Long movementId;
    private String movementDescription;
    private Long processFlowId;
    private String processFlowName;
    private Integer sequence;
    private Boolean isActive;
    private Boolean isOptional;
    private Boolean isSapPosting;
    private Boolean isTripComplete;
    private Boolean isDecisionPoint;
    private Long plantId;
    private String plantCode;
    private Long cargoTypeId;
    private String cargoType;
}
