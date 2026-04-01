package com.sipl.ticket.report.controller;

import com.sipl.ticket.core.dto.request.ActivityLogReportRequestDto;
import com.sipl.ticket.core.dto.request.ResolutionPenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.StaffTicketRequestDTO;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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


    @PostMapping("/staff-ticket")
    @ApiOperation(
            value = "Search Response Staff ticket Report",
            notes = "Fetch response staff ticket report with pagination"
    )
    public ResponseEntity<ApiResponseDTO<PagedResponse<StaffTicketResponseDTO>>>
    staffTicketReport(
            @RequestBody StaffTicketRequestDTO requestDto
    );

    @PostMapping("/response-penalty/export")
    @ApiOperation("Export Response Penalty Report (Excel / CSV / PDF)")
    ResponseEntity<Void> exportResponsePenaltyReport(
            @RequestBody ResponsePenaltyRequestDTO requestDto,
            @RequestParam String format,
            HttpServletResponse response
    );

    @PostMapping("/resolution-penalty")
    @ApiOperation(
            value = "Search Resolution Penalty Report",
            notes = "Fetch resolution penalty report with pagination"
    )
    public ResponseEntity<ApiResponseDTO<PagedResponse<ResolutionPenaltyResponseDTO>>>
    searchResolutionPenaltyReport(
            @RequestBody ResolutionPenaltyRequestDTO requestDto
    );

    @PostMapping("/resolution-penalty/export")
    ResponseEntity<Void> exportResolutionPenaltyReport(
            @RequestBody ResolutionPenaltyRequestDTO requestDto,
            @RequestParam String format,
            HttpServletResponse response
    );

    @PostMapping("/activity-log-report")
    @ApiOperation(
            value = "Search Activity Log Report",
            notes = "Fetch activity log report with pagination and date filter"
    )
    ResponseEntity<ApiResponseDTO<PagedResponse<ActivityLogReportResponseDto>>>
    searchActivityLogReport(
            @RequestBody ActivityLogReportRequestDto requestDto
    );

}
