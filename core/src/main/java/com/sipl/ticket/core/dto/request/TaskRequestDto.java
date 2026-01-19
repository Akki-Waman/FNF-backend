package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
    private Long TaskId;
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
    private String comment;
    private Integer branchId;
    private Long ticketId;

    private List<Long> assigneeUserIds;
    private List<Long> followerUserIds;
    private List<Long> tagIds;
}
