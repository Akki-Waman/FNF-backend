package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkFlowDefinitionDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface WorkFlowDefinitionService {
    ApiResponseDTO<WorkFlowDefinitionDTO> addWorkFlowDefinition(WorkFlowDefinitionDTO workFlowDefinitionDto);

    ApiResponseDTO<WorkFlowDefinitionDTO> updateWorkFlowDefinition(WorkFlowDefinitionDTO workFlowDefinitionDto);

    ApiResponseDTO<WorkFlowDefinitionDTO> getWorkFlowDefinitionById(Integer id);

    ApiResponseDTO<WorkFlowDefinitionDTO> getAllWorkFlowDefinitions();

    ApiResponseDTO<Void> deleteWorkFlowDefinitionById(Integer id);

    ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>> searchWorkFlowDefinitionsByPagination(WorkFlowDefinitionSearchRequestDTO request);

    void exportWorkFlowDefinitionCsv(WorkFlowDefinitionSearchRequestDTO request, HttpServletResponse response);

}
