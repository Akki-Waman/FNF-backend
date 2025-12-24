package com.sipl.collaboration.workflow.service;

import com.sipl.collaboration.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowInstanceDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface WorkFlowInstanceService {

    public ApiResponseDTO<WorkflowInstanceDTO> addWorkflowInstance(WorkflowInstanceDTO dto);

    public ApiResponseDTO<WorkflowInstanceDTO> updateWorkflowInstance(WorkflowInstanceDTO dto);

    ApiResponseDTO<PagedResponse<WorkflowInstanceDTO>> searchWorkflowInstancesByPagination(WorkflowInstanceSearchDTO searchDto, HttpServletRequest servletRequest);

    ApiResponseDTO<WorkflowInstanceDTO> deleteWorkflowInstanceById(Integer id);
}
