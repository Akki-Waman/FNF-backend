package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAssignmentInfoDTO {
    private Long orderAssignmentId;
    private Long childOrderId;
    private String childOrderNumber;
    private String customerName;
    private String materialName;
}
