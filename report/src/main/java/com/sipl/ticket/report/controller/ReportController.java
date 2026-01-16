package com.sipl.ticket.report.controller;

import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/reports")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Report APIs")
public interface ReportController {

    @PostMapping("/response-penalty")
    @ApiOperation(
            value = "Search Response Penalty Report",
            notes = "Fetch response penalty report with pagination"
    )
    public ResponseEntity<ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>>>
    searchResponsePenaltyReport(
            @RequestBody ResponsePenaltyRequestDTO requestDto
    );
}
