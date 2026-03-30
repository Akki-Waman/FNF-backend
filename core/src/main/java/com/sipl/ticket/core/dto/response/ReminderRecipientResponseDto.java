package com.sipl.ticket.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReminderRecipientResponseDto {

    private Long userId;

    private String channelType;
    private String status;

    private LocalDateTime sentAt;
}