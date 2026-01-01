package com.sipl.ticket.core.dao.entity;

import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketAttachmentResponseDTO {
    private Long attachmentId;
    private TicketsResponseDTO ticket;
    private String fileName;
    private String filePath;
    private Integer fileSizeKB;
    private String contentType;
    private UsersResponseDTO uploadedBy;
    private LocalDateTime uploadedOn;
}
