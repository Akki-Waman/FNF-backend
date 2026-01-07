package com.sipl.ticket.core.dto.response;

import lombok.Data;

@Data
public class TaskAssigneeCustomResponseDTO {
    private Long taskAssigneeId;
    private Long userId;
    private String userName;
    private Long taskId;
    private String subject;
}
