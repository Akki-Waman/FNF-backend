package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesOrderResponseDTO extends AuditDto implements Serializable {
    private Long id;
    private String salesOrderNumber;
    private LocalDate orderDate;
    private String customerName;
    private String poNumber;
    private LocalDate poDate;
    private String panNumber;
    private String gstRegistrationNumber;
    private String deliveryAddress;
    private LocalDate deliveryDate;
    private String remarks;
    private String paymentTerms;
    private BigDecimal deliveryCharge;
    private BigDecimal finalAmount;
    private Boolean isDraft;
    private String draftNumber;
    // Sales order items
    private List<OrderLineItemsResponseDTO> items;
}
