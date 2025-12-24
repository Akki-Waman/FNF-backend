package com.sipl.collaboration.workflow.controller;

import com.sipl.collaboration.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowNotificationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/workflow-notification")
@CrossOrigin("*")
@Api(tags = "Workflow Notification APIs")
public interface WorkflowNotificationController {
    @PostMapping("/search")
    @ApiOperation(value = "Search Workflow notifications with filters and pagination")
    ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>>> searchWorkFlowNotificationByPagination(
            @RequestBody WorkflowNotificationRequestDTO request);

    @PutMapping("/get/{workFlowNotificationId}")
    @ApiOperation(value = "Resend mail approval status by notification id")
    ResponseEntity<ApiResponseDTO<Void>> resendMail(
            @PathVariable("workFlowNotificationId") Integer workFlowNotificationId);
}
