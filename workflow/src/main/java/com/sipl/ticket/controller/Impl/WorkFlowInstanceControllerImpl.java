package com.sipl.ticket.controller.Impl;


import com.sipl.ticket.controller.WorkFlowInstanceController;
import com.sipl.ticket.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import com.sipl.ticket.service.WorkFlowInstanceService;
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
