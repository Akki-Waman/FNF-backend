package com.sipl.ticket.core.dto.response;

import lombok.Data;

@Data
public class TaskTagCustomResponseDTO {
    private Long taskTagId;
    private Long tagId;
    private String tagName;
    private Long taskId;
    private String subject;
}
