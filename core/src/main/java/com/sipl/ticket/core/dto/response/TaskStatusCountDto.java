package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusCountDto {

    private Integer statusId;
    private String statusName;
    private Long totalCount;
    private Long assignedTaskCountForUser;

}
