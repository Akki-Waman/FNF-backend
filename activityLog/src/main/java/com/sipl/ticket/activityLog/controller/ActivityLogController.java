package com.sipl.ticket.activityLog.controller;

import com.sipl.ticket.core.dto.request.ActivityLogReportRequestDto;
import com.sipl.ticket.core.dto.request.ClientProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/activity-logs")
@CrossOrigin("*")
@Api(tags = "Activity Log APIs")
public interface ActivityLogController {

    @ApiOperation(
            value = "Get latest activity logs",
            notes = "Admin will get all latest 10 logs, user will get own latest 10 logs",
            response = ActivityLogDashboardDto.class
    )
    @GetMapping("/latest")
    ResponseEntity<ApiResponseDTO<ActivityLogDashboardDto>> getLatestActivities();

    @PostMapping("/export")
    ResponseEntity<Void> exportActivityLogs(
            @RequestBody ActivityLogReportRequestDto requestDto,
            @RequestParam String format,
            HttpServletResponse response
    );


}
