package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadingUnloadingReportResponseDTO {

    private Long transactionStageId;
    private Long transactionId;
    private LocalDateTime actionTime;
    private String operatorName;
    private String lepNumber;
    private String tagNumber;
    private String truckNumber;
    private String plantCode;
    private String driverName;
    private Boolean isCompleted;
    private Boolean isActive;
    private String remarks;
    private String processType;
    private Long plantId;
    private Integer transactionStatus;
    private Boolean isTripCompleted;
    private LocalDateTime transactionCreatedOn;
}

