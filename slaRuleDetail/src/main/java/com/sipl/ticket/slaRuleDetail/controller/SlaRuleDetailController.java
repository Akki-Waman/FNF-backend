package com.sipl.ticket.slaRuleDetail.controller;

import com.sipl.ticket.core.dto.request.SlaRuleDetailsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "SLA Rule Details APIs")
@RequestMapping("/api/v1/sla-rule-details")
@CrossOrigin("*")
public interface SlaRuleDetailController {

    @ApiOperation(value = "Create SLA Rule Details")
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> save(
            @RequestBody SlaRuleDetailsDto dto
    );

    @ApiOperation(value = "Update SLA Rule Details")
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> update(
            @RequestBody SlaRuleDetailsDto dto
    );

    @ApiOperation(value = "Get SLA Rule Details by ID")
    @GetMapping("/get/{id}")
    ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> getById(
            @PathVariable Integer id
    );

    @ApiOperation(value = "Delete SLA Rule Details")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<ApiResponseDTO<String>> delete(
            @PathVariable Integer id
    );

    @ApiOperation(value = "Get all SLA Rule Details")
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> getAll();

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<SlaRuleDetailsDto>>> search(
            @RequestBody SlaRuleDetailsSearchRequestDto dto
    );

    @PostMapping("/export")
    ResponseEntity<byte[]> exportSlaRuleDetailsExcel(
            @RequestBody SlaRuleDetailsSearchRequestDto request);

}
