package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransporterCustomerReportResponseDTO {
    private String orderNo;
    private String childOrderNo;
    private String transporterName;
    private String customerName;
    private String materialType;
    private String materialName;
    //private String packageName;
    //private Integer noOfBags;
    private BigDecimal totalWeight;
    private String uom;
    private String status;
}
