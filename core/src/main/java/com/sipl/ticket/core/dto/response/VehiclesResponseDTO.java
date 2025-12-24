package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehiclesResponseDTO extends AuditDto{

    private Long id;
    private String vehicleRegistrationNumber;
    private String ownerName;
    private LocalDate registrationDate;
    private LocalDate rtoFitnessExpiryDate;
    private LocalDate rtoVehiclePermitExpiry;
    private LocalDate insuranceExpiryDate;
    private LocalDate pucValidity;
    private String regGrossWeight;
    private String regTareWeight;
    private String regCarryingCapacity;
    private String vehicleAge;
    private Long transporterId;
    private String companyName;
    private String transporterCategory;
    private Boolean isBlock;
    private Boolean isActive;
    private String imeiNumber;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private String gpsId;
    private String gpsCompany;
    private String material;
    private String lastTripLepStatus;
}

