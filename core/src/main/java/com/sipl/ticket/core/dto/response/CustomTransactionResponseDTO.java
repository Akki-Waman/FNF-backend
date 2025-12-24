package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTransactionResponseDTO {
    private Long transactionId;
    private String tagNumber;
    private Long tagId;
    private String lepNumber;
    private String transporterName;
    private String customerName;
    private String vehicleNumber;
    private String shipmentNumber;
    private String materialName;
    private List<BigDecimal> grossWeight;
    private List<BigDecimal> tareWeight;
    private List<BigDecimal> netWeight;
    private String doNumber;
    private List<String> salesOrderNumber;
    private List<String> childOrderNumber;
    private String operationType;
}



