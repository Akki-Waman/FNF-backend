package com.sipl.ticket.core.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReminderCreateRequestDto {
    private Long ticketReminderId;

    private Long ticketId;
    private LocalDateTime reminderTime;

    private Boolean isRecurring;
    private Integer recurrenceInterval;
    private LocalDateTime recurrenceEndTime;
    private boolean isActive;
    private boolean isDeleted;
    private List<ReminderRecipientRequestDto> recipients;
}