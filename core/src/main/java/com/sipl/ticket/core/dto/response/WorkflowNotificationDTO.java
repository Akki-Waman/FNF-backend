package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipl.ticket.core.dao.entity.Users;
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
public class WorkflowNotificationDTO extends AuditDto{
    private Integer workflowNotificationId;
    private WorkflowInstanceDTO instance;
    private WorkflowStepsDTO step;
    private Users user;
    private String notificationType;
    private String message;
    private String status;
    private LocalDateTime sentAt;
    private String notificationReason;
}
