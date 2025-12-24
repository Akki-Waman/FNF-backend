package com.sipl.ticket.core.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildOrderInfoDTO {
    private Long childOrderId;
    private String childOrderNumber;
    private Long orderAssignmentId;
}