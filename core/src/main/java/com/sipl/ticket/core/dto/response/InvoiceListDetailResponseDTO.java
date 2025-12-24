package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListDetailResponseDTO {
    private Long transactionId;
    private String lepNumber;
    private String orderNumber;
    private LocalDate orderDate;
    private String transporterName;
    private String doPoNumber;
    private BigDecimal orderQty;
    private String materialType;
    private String materialName;
    private BigDecimal netQty;
    private String vehicleNumber;
    private String status;
}
