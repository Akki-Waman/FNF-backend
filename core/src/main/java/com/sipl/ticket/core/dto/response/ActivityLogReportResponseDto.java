package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogReportResponseDto {

    private Long activityLogId;
    private String description;
    private String staffName;
    private String performedBy;
    private String ipAddress;
    private LocalDateTime createdTime;
}
