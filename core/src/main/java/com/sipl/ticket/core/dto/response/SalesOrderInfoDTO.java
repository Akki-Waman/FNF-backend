package com.sipl.ticket.core.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderInfoDTO {
    private Long salesOrderId;
    private String salesOrderNumber;
    private String customerName;
}