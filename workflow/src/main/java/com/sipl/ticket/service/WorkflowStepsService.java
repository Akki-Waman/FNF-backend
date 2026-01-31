package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.request.WorkFlowStepsSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;

import javax.servlet.http.HttpServletResponse;

public interface WorkflowStepsService {
    ApiResponseDTO<WorkflowStepsDTO> addWorkflowSteps(WorkflowStepsDTO workflowStepsDto);

    ApiResponseDTO<WorkflowStepsDTO> updateWorkflowSteps(WorkflowStepsDTO workflowStepsDto);

    ApiResponseDTO<WorkflowStepsDTO> getWorkFlowStepsById(Integer id);

    ApiResponseDTO<WorkflowStepsDTO> getAllWorkFlowSteps();

    ApiResponseDTO<Void> deleteWorkFlowStepsById(Integer id);

    ApiResponseDTO<PagedResponse<WorkflowStepsDTO>> searchWorkFlowStepsByPagination(WorkFlowStepsSearchRequestDTO request);

    void exportWorkflowStepsCsv(WorkFlowStepsSearchRequestDTO request, HttpServletResponse response);

}
