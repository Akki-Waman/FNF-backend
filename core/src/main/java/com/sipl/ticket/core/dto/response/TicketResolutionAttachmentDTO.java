package com.sipl.ticket.core.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResolutionAttachmentDTO extends AuditDto {

    private Long attachmentId;

    private TicketResolutionDTO ticketResolution;

    private String fileName;

    private String filePath;

    private Integer fileSizeKB;

    private String contentType;

    private LocalDateTime uploadedOn;
}

