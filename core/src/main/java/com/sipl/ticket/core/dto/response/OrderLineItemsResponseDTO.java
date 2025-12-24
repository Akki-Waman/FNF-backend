package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderLineItemsResponseDTO extends AuditDto{

    private Long id;
    private String itemCode;
    private String itemName;
    private BigDecimal quantity;
    private String uom;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    private String orderLineItemNumber;
    private Integer status;
    private SalesOrderResponseDTO salesOrder;
    private LocalDate orderDate;
    private BigDecimal totalOrderSplit;
    private BigDecimal balance;

}
