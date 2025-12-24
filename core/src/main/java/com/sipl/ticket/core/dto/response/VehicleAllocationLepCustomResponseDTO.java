package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAllocationLepCustomResponseDTO {
    private Long vehicleAllocationId;
    private String lepNumber;
    private List<OrderAssignmentInfoDTO> orderAssignments;
    private String vehicleRegistrationNumber;
    private Long vehicleId;
    private Long driverId;
    private String driverName;
}
