package com.sipl.ticket.controller.Impl;


import com.sipl.ticket.controller.WorkFlowDefinitionController;
import com.sipl.ticket.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkFlowDefinitionDTO;
import com.sipl.ticket.service.WorkFlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkFlowDefinitionControllerImpl implements WorkFlowDefinitionController {

    private final WorkFlowDefinitionService workFlowDefinitionService;

    @Override
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> addWorkFlowDefinition(
            WorkFlowDefinitionDTO workFlowDefinitionDto) {
        log.info("<<START>> addWorkFlowDefinition called <<START>>");
        ApiResponseDTO<WorkFlowDefinitionDTO> apiResponse =
                workFlowDefinitionService.addWorkFlowDefinition(workFlowDefinitionDto);
        ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> addWorkFlowDefinition <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> updateWorkFlowDefinition(
            WorkFlowDefinitionDTO workFlowDefinitionDto) {
        log.info("<<START>> updateWorkFlowDefinition called <<START>>");
        ApiResponseDTO<WorkFlowDefinitionDTO> apiResponse =
                workFlowDefinitionService.updateWorkFlowDefinition(workFlowDefinitionDto);
        ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> updateWorkFlowDefinition <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> getWorkFlowDefinitionById(Integer id) {
        log.info("<<START>> getWorkFlowDefinitionById called for ID: {} <<START>>", id);
        ApiResponseDTO<WorkFlowDefinitionDTO> apiResponse =
                workFlowDefinitionService.getWorkFlowDefinitionById(id);
        log.info("<<END>> getWorkFlowDefinitionById <<END>>");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> getAllWorkFlowDefinitions() {
        log.info("<<START>> getAllWorkFlowDefinitions called <<START>>");
        ApiResponseDTO<WorkFlowDefinitionDTO> apiResponse =
                workFlowDefinitionService.getAllWorkFlowDefinitions();
        log.info("<<END>> getAllWorkFlowDefinitions <<END>>");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> deleteWorkFlowDefinitionById(Integer id) {
        log.info("<<START>> deleteWorkFlowDefinitionById called for ID: {} <<START>>", id);
        ApiResponseDTO<Void> apiResponse = workFlowDefinitionService.deleteWorkFlowDefinitionById(id);
        log.info("<<END>> deleteWorkFlowDefinitionById <<END>>");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>>> searchWorkFlowDefinitionsByPagination(
            WorkFlowDefinitionSearchRequestDTO request) {
        log.info("<<START>> searchWorkFlowDefinitionsByPagination <<START>>");
        ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>> response =
                workFlowDefinitionService.searchWorkFlowDefinitionsByPagination(request);
        log.info("<<END>> searchWorkFlowDefinitionsByPagination <<END>>");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
