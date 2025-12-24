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
public class DriversResponseDTO extends AuditDto{
    private Long id;
    private Integer driverCode;
    private String driverName;
    private LocalDate dob;
    private String driverMobileNo;
    private String driverLicenseNo;
    private LocalDate driverLicenseExpiryDate;
    private String driverAadhaarNo;
    private Boolean plantSafetyInduction;
    private LocalDate driverLicenseExpiryDateHazardous;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private Long transportersId;
    private String companyName;
    private Boolean isActive;
    private String value;
    private String age;
}
