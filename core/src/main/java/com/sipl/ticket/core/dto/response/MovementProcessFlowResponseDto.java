package com.sipl.ticket.core.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MovementProcessFlowResponseDto {
    private String movementName;
    private List<ProcessStepDto> processSteps;

    public MovementProcessFlowResponseDto(String movementName, List<ProcessStepDto> processSteps) {
        this.movementName = movementName;
        this.processSteps = processSteps;
    }
}
