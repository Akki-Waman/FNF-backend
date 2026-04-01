package com.sipl.ticket.controller.Impl;


import com.sipl.ticket.controller.WorkflowStepsController;
import com.sipl.ticket.core.dto.request.WorkFlowStepsSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;
import com.sipl.ticket.service.WorkflowStepsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkflowStepsControllerImpl implements WorkflowStepsController {
    private final WorkflowStepsService workflowStepsService;

    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> addWorkflowSteps(
            WorkflowStepsDTO workflowStepsDto) {
        log.info("<<START>> addWorkflowSteps called <<START>>");
        ApiResponseDTO<WorkflowStepsDTO> apiResponse =
                workflowStepsService.addWorkflowSteps(workflowStepsDto);
        ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> addWorkflowSteps <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> updateWorkflowSteps(
            WorkflowStepsDTO workflowStepsDto) {
        log.info("<<START>> updateWorkflowSteps called <<START>>");
        ApiResponseDTO<WorkflowStepsDTO> apiResponse =
                workflowStepsService.updateWorkflowSteps(workflowStepsDto);
        ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> updateWorkflowSteps <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> getWorkFlowStepsById(Integer id) {
        log.info("<<START>> getWorkFlowStepsById called <<START>>");
        ApiResponseDTO<WorkflowStepsDTO> apiResponse = workflowStepsService.getWorkFlowStepsById(id);
        ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> getWorkFlowStepsById <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> getAllWorkFlowSteps() {
        log.info("<<START>> getAllWorkFlowSteps called <<START>>");
        ApiResponseDTO<WorkflowStepsDTO> apiResponse = workflowStepsService.getAllWorkFlowSteps();
        ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> getAllWorkFlowSteps <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> deleteWorkFlowStepsById(Integer id) {
        log.info("<<START>> deleteWorkFlowStepsById called <<START>>");
        ApiResponseDTO<Void> apiResponse = workflowStepsService.deleteWorkFlowStepsById(id);
        ResponseEntity<ApiResponseDTO<Void>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> deleteWorkFlowStepsById <<END>>");
        return responseEntity;
    }
    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowStepsDTO>>> searchWorkFlowStepsByPagination(WorkFlowStepsSearchRequestDTO request) {
        log.info("<<START>> searchWorkFlowDefinitionsByPagination <<START>>");
        ApiResponseDTO<PagedResponse<WorkflowStepsDTO>> response =
                workflowStepsService.searchWorkFlowStepsByPagination(request);
        log.info("<<END>> searchWorkFlowDefinitionsByPagination <<END>>");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> exportWorkflowStepsCsv(
            WorkFlowStepsSearchRequestDTO request,
            HttpServletResponse response) {

        log.info("<<Start>> exportWorkflowStepsCsv");

        workflowStepsService
                .exportWorkflowStepsCsv(request, response);

        log.info("<<End>> exportWorkflowStepsCsv");

        return ResponseEntity.ok().build();
    }

}
