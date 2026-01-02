package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends AuditDto{

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

    private Integer status;

    private BranchDto branch;

    private TaskDto ticket;
}
