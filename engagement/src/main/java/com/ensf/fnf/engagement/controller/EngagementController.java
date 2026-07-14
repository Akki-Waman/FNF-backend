package com.ensf.fnf.engagement.controller;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.IntelligenceMetricsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/intelligence")
@Api(tags = "Relationship Intelligence Dashboard API")
public interface EngagementController {

    @GetMapping("/metrics")
    @ApiOperation("Fetch aggregated intimacy scores and engagement telemetry")
    ResponseEntity<CommonApiResponse<IntelligenceMetricsDto>> getDashboardMetrics();

    @GetMapping("/celebrations")
    @ApiOperation("Fetch upcoming birthdays and anniversaries for the dashboard")
    ResponseEntity<CommonApiResponse<java.util.List<Object>>> getUpcomingCelebrations();

    @GetMapping("/notifications")
    @ApiOperation("Fetch in-app user activity notifications")
    ResponseEntity<CommonApiResponse<java.util.List<Object>>> getNotifications();
}