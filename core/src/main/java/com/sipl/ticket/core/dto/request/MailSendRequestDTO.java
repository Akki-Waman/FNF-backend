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
    private String transactionId;
    private String status;
    private String subject;
    private LocalDate date;
    private String performedBy;
    private String cancellationReason;
    private LocalDateTime lepIssueDate;
    private String lepNumber;
    private String cancellationRequestBy;
    private LocalDateTime cancellationInititatedTime;
    private String truckNumber;
    private String driverName;
    private String batchNumber;
    private String rfidNumber;
    private Long templateId;

}
