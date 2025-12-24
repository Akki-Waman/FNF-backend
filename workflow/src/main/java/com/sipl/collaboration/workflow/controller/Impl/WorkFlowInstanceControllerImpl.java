package com.sipl.collaboration.workflow.controller.Impl;

import com.sipl.collaboration.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowInstanceDTO;
import com.sipl.collaboration.workflow.controller.WorkFlowInstanceController;
import com.sipl.collaboration.workflow.service.WorkFlowInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class WorkFlowInstanceControllerImpl implements WorkFlowInstanceController {
    private final WorkFlowInstanceService workFlowInstanceService;

    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowInstanceDTO>> deleteWorkflowInstanceById(Integer id) {
        return ResponseEntity.ok(workFlowInstanceService.deleteWorkflowInstanceById(id));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowInstanceDTO>>> searchWorkflowInstancesByPagination(WorkflowInstanceSearchDTO searchDto, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(workFlowInstanceService.searchWorkflowInstancesByPagination(searchDto,servletRequest));
    }
}
