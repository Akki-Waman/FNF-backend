package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dto.request.TaskAttachmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombinedTaskResponseDto {

    private TaskDto task;

    private List<TaskAssigneeDto> assignees;

    private List<TaskFollowerDto> followers;

    private List<TaskTagDto> tags;

    private List<TaskAttachmentDto> attachments;
}