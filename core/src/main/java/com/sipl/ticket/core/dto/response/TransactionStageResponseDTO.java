package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStageResponseDTO extends AuditDto{
    private Long id;

    private Long transactionId;

    private Long movementFlowMappingId;

    private String plantCode;

    private LocalDateTime actionTime;

    private String actionBy;

    private String location;

    private String remarks;

    private Boolean isCompleted;

    private Boolean isActive;

    private Long entityId;

    private Long masterId;

    private String masterName;

    private String processFlowName;
}
