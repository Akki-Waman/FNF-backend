package com.sipl.ticket.core.dto.response;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TaskExportDTO {
    private Long taskId;
    private String taskName;
    private String status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String assignedTo;
    private String tags;
    private String priority;
}
