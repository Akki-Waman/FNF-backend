package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.WorkflowActionDTO;
import org.springframework.stereotype.Service;

@Service
public interface WorkflowActionService {
    ApiResponseDTO<WorkflowActionDTO> processWorkflowAction(WorkflowActionDTO workflowActionDto);

}
