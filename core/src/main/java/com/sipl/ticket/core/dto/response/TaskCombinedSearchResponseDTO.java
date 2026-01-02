package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCombinedSearchResponseDTO {
    private TaskCustomResponseDTO taskCustomResponseDTO;
    private List<TaskAssigneeCustomResponseDTO> taskAssigneeCustomResponseDTOS;
    private List<TaskFollowerCustomResponseDTO> taskFollowerCustomResponseDTOS;
    private List< TaskTagCustomResponseDTO> taskTagCustomResponseDTOS;
    private List <TaskAttachmentCustomResponseDTO> taskAttachmentCustomResponseDTOS;
}
