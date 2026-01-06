package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryDto {

    private Long notStarted;
    private Long inProgress ;
    private Long testing;
    private Long awaitingFeedback;
    private Long complete;

}
