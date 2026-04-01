package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowNotificationDTO;

import javax.servlet.http.HttpServletResponse;

public interface WorkflowNotificationService {
    public ApiResponseDTO<WorkflowNotificationDTO> addWorkflowNotification(
            WorkflowNotificationDTO workflowNotificationDto);


    ApiResponseDTO<Void> resendMail(Integer workFlowNotificationId);

    ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>> searchWorkFlowStepsByPagination(WorkflowNotificationRequestDTO request);

    void exportWorkflowNotificationsCsv(WorkflowNotificationRequestDTO request, HttpServletResponse response);

}
