package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAssignmentDTO {
    private Long orderAssignmentId;
    private Long saleOrderId;
    private String saleOrderNumber;
    private String childOrder;
    private BigDecimal qty;
}