package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowNotificationRequestDTO {
    private Integer pageNum;
    private Integer pageSize;
    private Integer workflowNotificationId;
    private String status;
    private String notificationType;
}
