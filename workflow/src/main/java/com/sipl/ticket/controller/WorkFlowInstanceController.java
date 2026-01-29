package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/workflow-instance")
@CrossOrigin("*")
@Api(tags = "Workflow Instance APIs")
public interface WorkFlowInstanceController {

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete Workflow Instance by ID")
    ResponseEntity<ApiResponseDTO<WorkflowInstanceDTO>> deleteWorkflowInstanceById(
            @PathVariable("id") Integer id);

    @PostMapping("/search")
    @ApiOperation(value = "Search Workflow Instances with filters and pagination")
    ResponseEntity<ApiResponseDTO<PagedResponse<WorkflowInstanceDTO>>> searchWorkflowInstancesByPagination(
            @RequestBody WorkflowInstanceSearchDTO searchDto, HttpServletRequest servletRequest);




}
