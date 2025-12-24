package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListResponseDTO {
    private Long transactionId;
    private String truckNumber;
    private String materialName;
    private String transporterName;
    private String status;
    private String value;
    private String lepNumber;
}
