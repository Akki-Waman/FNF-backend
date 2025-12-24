package com.sipl.collaboration.workflow.controller.Impl;

import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.WorkflowActionDTO;
import com.sipl.collaboration.workflow.controller.WorkflowActionController;
import com.sipl.collaboration.workflow.service.WorkflowActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkflowActionControllerImpl implements WorkflowActionController {
private final WorkflowActionService workflowActionService;
    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowActionDTO>> processWorkflowAction(WorkflowActionDTO workflowActionDto) {
        return ResponseEntity.ok(workflowActionService.processWorkflowAction(workflowActionDto));
    }
}
