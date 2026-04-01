package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class ReminderRecipientRequestDto {

    private Long userId;
    private String channelType;
    private boolean isActive;
    private boolean isDeleted;
}