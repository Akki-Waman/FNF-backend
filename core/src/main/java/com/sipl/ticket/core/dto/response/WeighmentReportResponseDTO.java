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
public class WeighmentReportResponseDTO {
    private Long transactionId;
    private String weighmentType;
    private String movementDescription;
    private String plantCode;
    private String tagNumber;
    private LocalDateTime gateInTime;
    private String truckNumber;
    private BigDecimal tareWeight;
    private String tareLocation;
    private BigDecimal grossWeight;
    private String grossLocation;
    private BigDecimal netWeight;
    private LocalDateTime tareTime;
    private LocalDateTime grossTime;
    private LocalDateTime netTime;
    private String tareBy;
    private String grossBy;
}
