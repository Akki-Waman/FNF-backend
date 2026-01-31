package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.WorkFlowStepsSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/workflow-step")
@CrossOrigin("*")
@Api(tags = "Workflow Step APIs")
public interface WorkflowStepsController {
    @PostMapping("/save")
    @ApiOperation(
            value = "Create a new Workflow Steps",
            notes = "Provide the necessary WorkflowSteps information to save a new entry",
            response = WorkflowStepsDTO.class)
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> addWorkflowSteps(
            @Valid @RequestBody WorkflowStepsDTO workflowStepsDto);

    @PutMapping("/update")
    @ApiOperation(
            value = "Update a existing Workflow Steps",
            notes = "Provide the necessary WorkflowSteps information to update a existing entry",
            response = WorkflowStepsDTO.class)
    public ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> updateWorkflowSteps(
            @RequestBody WorkflowStepsDTO workflowStepsDto);

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Get Workflow Steps by ID")
    ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> getWorkFlowStepsById(
            @PathVariable("id") Integer id);

    @GetMapping("/all")
    @ApiOperation(value = "Get all Workflow Steps")
    ResponseEntity<ApiResponseDTO<WorkflowStepsDTO>> getAllWorkFlowSteps();

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete Workflow Steps by ID")
    ResponseEntity<ApiResponseDTO<Void>> deleteWorkFlowStepsById(@PathVariable("id") Integer id);

    @PostMapping("/search")
    @ApiOperation(value = "Search Workflow Steps with filters and pagination")
    ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowStepsDTO>>> searchWorkFlowStepsByPagination(
            @RequestBody WorkFlowStepsSearchRequestDTO request);

    @PostMapping("/export")
    ResponseEntity<Void> exportWorkflowStepsCsv(
            @RequestBody WorkFlowStepsSearchRequestDTO request,
            HttpServletResponse response);

}
