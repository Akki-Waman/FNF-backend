package com.sipl.ticket.core.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateVehicleResponseDTO {
    private List<SalesOrderInfoDTO> salesOrders;
    private List<ChildOrderInfoDTO> childOrders;
    private Long transporterId;
    private String transporterName;
    private BigDecimal totalQty;
    private BigDecimal salesOrderQuantity;
    private Long transporterTypeId;
    private String transporterTypeCode;
}
