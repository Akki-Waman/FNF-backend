package com.sipl.collaboration.workflow.service;

import com.sipl.collaboration.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkFlowDefinitionDTO;
import org.springframework.stereotype.Service;

@Service
public interface WorkFlowDefinitionService {
    ApiResponseDTO<WorkFlowDefinitionDTO> addWorkFlowDefinition(WorkFlowDefinitionDTO workFlowDefinitionDto);

    ApiResponseDTO<WorkFlowDefinitionDTO> updateWorkFlowDefinition(WorkFlowDefinitionDTO workFlowDefinitionDto);

    ApiResponseDTO<WorkFlowDefinitionDTO> getWorkFlowDefinitionById(Integer id);

    ApiResponseDTO<WorkFlowDefinitionDTO> getAllWorkFlowDefinitions();

    ApiResponseDTO<Void> deleteWorkFlowDefinitionById(Integer id);

    ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>> searchWorkFlowDefinitionsByPagination(WorkFlowDefinitionSearchRequestDTO request);
}
