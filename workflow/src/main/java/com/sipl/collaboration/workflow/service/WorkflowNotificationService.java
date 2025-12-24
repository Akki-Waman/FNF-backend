package com.sipl.collaboration.workflow.service;

import com.sipl.collaboration.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowNotificationDTO;

public interface WorkflowNotificationService {
    public ApiResponseDTO<WorkflowNotificationDTO> addWorkflowNotification(
            WorkflowNotificationDTO workflowNotificationDto);

    ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>> searchWorkFlowStepsByPagination(WorkflowNotificationRequestDTO request);

    ApiResponseDTO<Void> resendMail(Integer workFlowNotificationId);
}
