package com.sipl.ticket.core.dto.response;

import lombok.Data;

@Data
public class TaskAttachmentCustomResponseDTO {
    private Long attachmentId;
    private Long taskId;
    private String subject;
    private Long documentId;

}
