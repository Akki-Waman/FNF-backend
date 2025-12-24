package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCalculationResponse {
    private BigDecimal subTotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal deliveryCharge;
    private BigDecimal grandTotal;
}
