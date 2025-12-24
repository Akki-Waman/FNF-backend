package com.sipl.collaboration.workflow.controller.Impl;

import com.sipl.collaboration.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowNotificationDTO;
import com.sipl.collaboration.workflow.controller.WorkflowNotificationController;
import com.sipl.collaboration.workflow.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
