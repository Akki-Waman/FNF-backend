package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoReportResponseDTO {
    private String orderNo;
    private Date orderDate;
    private String customerName;
    private String materialType;
    private String materialCode;
    private String materialName;
    private BigDecimal orderQty;
    private String uom;
    private String status;
}

