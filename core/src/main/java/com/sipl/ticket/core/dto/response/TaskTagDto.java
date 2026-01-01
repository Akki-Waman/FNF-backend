package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dto.request.TagsRequestDto;
import lombok.Data;

@Data
public class TaskTagDto extends AuditDto{

    private Long taskTagId;

    private TaskDto task;

    private TagsRequestDto tag;
}
