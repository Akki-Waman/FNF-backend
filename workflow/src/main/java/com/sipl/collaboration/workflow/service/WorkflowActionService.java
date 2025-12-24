package com.sipl.collaboration.workflow.service;

import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.WorkflowActionDTO;
import org.springframework.stereotype.Service;

@Service
public interface WorkflowActionService {
    ApiResponseDTO<WorkflowActionDTO> processWorkflowAction(WorkflowActionDTO workflowActionDto);

}
