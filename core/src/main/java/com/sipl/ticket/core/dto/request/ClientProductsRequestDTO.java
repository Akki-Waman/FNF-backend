package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientProductsRequestDTO {

    private ProductDto product;
    private String groupName;
    private RegionResponseDTO region;
    private ZoneResponseDTO zone;
    private DivisionResponseDTO division;
    private OperationalUnitResponseDTO unit;
    private String deviceName;
    private String platformModel;
    private String serialNumber;
    private String imeiNo;
    private String mdmAssetNo;
    private String partNo;
    private String workingStatus;
    private String deviceStatus;
    private String poNo;
    private LocalDate poDate;
    private String remark1;
    private String remark2;
    private Boolean isWarranty = false;
    private LocalDate warrantyPeriodStartDate;
    private LocalDate warrantyPeriodEndDate;
    private Boolean isActive = true;
    private Integer branchId;
    private String branchName;
    private String deliveryNumber;
    private LocalDate deliveryDate;
}
