package com.sipl.ticket.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReminderResponseDto extends AuditDto {

    private Long id;
    private Long ticketId;

    private LocalDateTime reminderTime;
    private LocalDateTime nextRunTime;
    private Boolean isRecurring;
    private Integer recurrenceInterval;
    private LocalDateTime recurrenceEndTime;

    private String status;

    private List<ReminderRecipientResponseDto> recipients;
}