package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkFlowDefinitionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/workflow-definition")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "WorkFlowDefinition APIs")
public interface WorkFlowDefinitionController {
    @PostMapping("/save")
    @ApiOperation(
            value = "Create a new Workflow Definition",
            notes = "Provide the necessary WorkflowDefinition information to save a new entry",
            response = WorkFlowDefinitionDTO.class)
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> addWorkFlowDefinition(
            @RequestBody WorkFlowDefinitionDTO workFlowDefinitionDto);

    @PutMapping("/update")
    @ApiOperation(
            value = "Update an existing Workflow Definition",
            notes = "Provide WorkFlowDefinitionId and other fields to update the workflow definition",
            response = WorkFlowDefinitionDTO.class)
    public ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> updateWorkFlowDefinition(
            @RequestBody WorkFlowDefinitionDTO workFlowDefinitionDto);

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Get Workflow Definition by ID")
    ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> getWorkFlowDefinitionById(
            @PathVariable("id") Integer id);

    @GetMapping("/all")
    @ApiOperation(value = "Get all Workflow Definitions")
    ResponseEntity<ApiResponseDTO<WorkFlowDefinitionDTO>> getAllWorkFlowDefinitions();

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete Workflow Definition by ID")
    ResponseEntity<ApiResponseDTO<Void>> deleteWorkFlowDefinitionById(@PathVariable("id") Integer id);

    @PostMapping("/search")
    @ApiOperation(value = "Search Workflow Definitions with filters and pagination")
    ResponseEntity<ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>>> searchWorkFlowDefinitionsByPagination(
            @RequestBody WorkFlowDefinitionSearchRequestDTO request);

    @PostMapping("/export")
    ResponseEntity<Void> exportWorkFlowDefinitionCsv(
            @RequestBody WorkFlowDefinitionSearchRequestDTO request,
            HttpServletResponse response);
}



