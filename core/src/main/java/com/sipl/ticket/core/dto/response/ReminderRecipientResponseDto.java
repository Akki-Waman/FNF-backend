package com.sipl.ticket.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReminderRecipientResponseDto extends AuditDto {

    private Long userId;

    private Integer channelType;
    private String channelTypeLabel;
    private Boolean isActive;
    private Boolean isDeleted;
}