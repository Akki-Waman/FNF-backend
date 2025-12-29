package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.Products;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProductsResponseDTO extends AuditDto{

    private Long clientProductId;

    private ProductDto product;

    private String groupName;

    private String region;

    private String zone;

    private String division;

    private String unit;

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

    private Boolean isActive = true;

}
