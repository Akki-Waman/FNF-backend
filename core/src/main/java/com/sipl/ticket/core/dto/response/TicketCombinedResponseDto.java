package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCombinedResponseDto extends AuditDto{

    private Long ticketId;
    private String subject;
    private String complaintName;
    private String complaintMobileNo;
    private Long departmentId;
    private String departmentName;
    private Long serviceId;
    private String serviceName;
    private String status;
    private String priority;
    private LocalDateTime lastReply;
    private List<Long> tagIds;
    private List<String> tagName;

}
