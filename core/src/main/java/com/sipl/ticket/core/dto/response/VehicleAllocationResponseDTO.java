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
public class VehicleAllocationResponseDTO extends AuditDto{

    private Long id;
    private Long orderAssignmentId;
    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private String vehicleOwnerName;
    private Long driverId;
    private Integer driverCode;
    private String driverName;
    private BigDecimal allocatedQty;
    private LocalDateTime allocationTime;
    private String lrNumber;
    private String lepNumber;
    private String doNumber;
    private String shipmentNumber;
    private Integer status;
    private String materialName;
    private String customerName;
    private BigDecimal orderQuantity;
}
