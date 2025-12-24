package com.sipl.collaboration.workflow.controller;

import com.sipl.collaboration.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkFlowDefinitionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
