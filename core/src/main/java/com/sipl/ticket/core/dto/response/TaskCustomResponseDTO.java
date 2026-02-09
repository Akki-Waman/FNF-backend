package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCustomResponseDTO {
    private Long taskId;

    private String subject;

    private String description;

    private Boolean isPublic;

    private Boolean isBillable;

    private BigDecimal hourlyRate;

    private LocalDate startDate;

    private LocalDate dueDate;

    private Integer priority;

    private String repeatType;

    private String relatedToType;

    private String status;

    private Integer branchId;
    private String branchName;

    private Long ticketId;
    private String ticketSubject;
    private LocalDateTime timerStartTime;
    private LocalDateTime timerStopTime;
}
