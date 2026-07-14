package com.ensf.fnf.app.controller;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/connections")
@Api(tags = "Connection Discovery & Network API")
public interface ConnectionDiscoveryController {

    @GetMapping("/discover")
    @ApiOperation("Suggest potential friends based on network graph intelligence")
    ResponseEntity<CommonApiResponse<List<Object>>> discoverPeople();

    @GetMapping("/requests")
    @ApiOperation("Fetch dual matrices of Pending Incoming and Sent Outgoing requests")
    ResponseEntity<CommonApiResponse<Object>> getPendingRequests();

    // NOTE: Send Request & Respond to Request were completed in the previous step.
}