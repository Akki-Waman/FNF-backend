package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailSendRequestDTO {
    private List<String> notifiedEmailId;
    private List<String> emailIds;
    private String ticketId;
    private String status;
    private String subject;
    private LocalDate date;
    private Long templateId;
    private String performedBy;
    private String initiatedBy;
    private String remarks;
    private String raisedReason;
    private String ticketSubject;

}
