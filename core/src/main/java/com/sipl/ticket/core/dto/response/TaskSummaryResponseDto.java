package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryResponseDto {
    private List<TaskStatusCountDto> overallSummary;
    private List<TaskStatusCountDto> userSummary;
}
