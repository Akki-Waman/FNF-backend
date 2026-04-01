package com.sipl.ticket.controller;



import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.WorkflowActionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/workflow-action")
@CrossOrigin("*")
@Api(tags = "Workflow Action APIs")
public interface WorkflowActionController {
    @PostMapping("/process")
    @ApiOperation(
            value = "Create a new Workflow Action and its update process",
            notes = "Provide necessary details to save a new WorkflowAction",
            response = WorkflowActionDTO.class)
    ResponseEntity<ApiResponseDTO<WorkflowActionDTO>> processWorkflowAction(
            @RequestBody WorkflowActionDTO workflowActionDto);
}
