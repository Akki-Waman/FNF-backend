package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponseDto extends AuditDto {

    private Long ticketReminderId;
    private Long ticketId;

    private LocalDateTime reminderTime;
    private LocalDateTime nextRunTime;
    private Boolean isRecurring;
    private Integer recurrenceInterval;
    private LocalDateTime recurrenceEndTime;

    private Integer status;
    private String statusLabel;
    private boolean isActive;
    private boolean isDeleted;

    private List<ReminderRecipientResponseDto> recipients;
}