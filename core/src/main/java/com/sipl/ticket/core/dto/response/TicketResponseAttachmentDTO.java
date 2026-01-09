package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseAttachmentDTO extends AuditDto{

    private Long attachmentId;

    private TicketResponseDTO ticketResponse;

    private String fileName;

    private String filePath;

    private Integer fileSizeKB;

    private String contentType;

    private LocalDateTime uploadedOn;
}
