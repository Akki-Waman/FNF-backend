package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchRequestDto {

    private Long taskId;
    private String subject;

    private Integer page = 0;
    private Integer size = 10;

    private String sortBy = "taskId";
    private String sortDir = "desc";
}
