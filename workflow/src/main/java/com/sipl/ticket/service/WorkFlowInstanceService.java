package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface WorkFlowInstanceService {

    public ApiResponseDTO<WorkflowInstanceDTO> addWorkflowInstance(WorkflowInstanceDTO dto);

    public ApiResponseDTO<WorkflowInstanceDTO> updateWorkflowInstance(WorkflowInstanceDTO dto);

    ApiResponseDTO<PagedResponse<WorkflowInstanceDTO>> searchWorkflowInstancesByPagination(WorkflowInstanceSearchDTO searchDto, HttpServletRequest servletRequest);

    ApiResponseDTO<WorkflowInstanceDTO> deleteWorkflowInstanceById(Integer id);

    void exportWorkflowInstanceCsv(
            WorkflowInstanceSearchDTO request,
            HttpServletRequest servletRequest,
            HttpServletResponse response);


}
