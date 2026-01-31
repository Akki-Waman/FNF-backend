package com.sipl.ticket.controller.Impl;


import com.sipl.ticket.controller.WorkflowNotificationController;
import com.sipl.ticket.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowNotificationDTO;
import com.sipl.ticket.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkflowNotificationControllerImpl implements WorkflowNotificationController {
    private final WorkflowNotificationService workflowNotificationService;

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>>>
    searchWorkFlowNotificationByPagination(WorkflowNotificationRequestDTO request) {
        log.info("<<START>> searchWorkFlowNotificationByPagination called <<START>>");
        ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>> apiResponse =
                workflowNotificationService.searchWorkFlowStepsByPagination(request);
        ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> searchWorkFlowNotificationByPagination <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> resendMail(Integer workFlowNotificationId) {
        log.info("<<START>> resendMail called <<START>>");
        ApiResponseDTO<Void> apiResponse = workflowNotificationService.resendMail(workFlowNotificationId);
        ResponseEntity<ApiResponseDTO<Void>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> resendMail <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Void> exportWorkflowNotificationsCsv(
            WorkflowNotificationRequestDTO request,
            HttpServletResponse response) {

        log.info("<<Start>> exportWorkflowNotificationsCsv");

        workflowNotificationService
                .exportWorkflowNotificationsCsv(request, response);

        log.info("<<End>> exportWorkflowNotificationsCsv");

        return ResponseEntity.ok().build();
    }

}
