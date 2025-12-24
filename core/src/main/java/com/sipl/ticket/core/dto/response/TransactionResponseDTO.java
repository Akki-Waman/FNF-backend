package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO extends AuditDto{
    private Long id;

    private Long vehicleAllocationId;

    private Long rfidId;

    private Long movementId;

    private String plantCode;

    private Integer flag;

    private Boolean isTripCompleted ;

    private Integer status ;

    private long version ;

    private Boolean isCancelled ;

    private String cancelReason;

    private LocalDateTime cancelTime;

    private String cancelledBy;

    private Boolean isActive;

    private Integer deliveryStatus;
}
