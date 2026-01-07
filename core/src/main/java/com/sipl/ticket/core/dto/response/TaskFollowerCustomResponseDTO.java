package com.sipl.ticket.core.dto.response;

import lombok.Data;

@Data
public class TaskFollowerCustomResponseDTO {
    private Long taskFollowerId;
    private Long userId;
    private String userName;
    private Long taskId;
    private String subject;
}
