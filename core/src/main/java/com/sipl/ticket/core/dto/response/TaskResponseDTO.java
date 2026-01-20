package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long taskId;
    private Long ticketId;
    private String taskSubject;
    private String ticketSubject;
    private String priorityLabel;
    private String statusLabel;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDateTime createdTime;
}
