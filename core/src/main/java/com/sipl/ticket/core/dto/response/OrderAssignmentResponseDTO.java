package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderAssignmentResponseDTO extends AuditDto{

    private Long id;
    private Long childOrderId;
    private String childOrderNumber;
    private Long transporterId;
    private String transporterName;
    private Long transporterCategoryId;
    private String transporterCategoryName;
    private LocalDateTime assignedTime;
    private LocalDateTime committedTime;
    private LocalDateTime expiryTime;
    private Integer status;
    private Long version;
    private UsersResponseDTO committedBy;
    private UsersResponseDTO cancelledBy;
    private LocalDateTime cancelledTimeStamp;
    private String salesOrderNumber;
    private String materialName;
    private Long childOrderQty;
    private String customerName;
}
