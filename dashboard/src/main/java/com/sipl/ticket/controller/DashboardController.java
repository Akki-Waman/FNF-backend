package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dashboard")
@CrossOrigin("*")
@Api(tags = "Dashboard APIs")
public interface DashboardController {
    @GetMapping("/ticket-status")
    ResponseEntity<ApiResponseDTO<ChartSeriesDTO>> getTicketStatus();

    @GetMapping("/ticket-priority")
    ResponseEntity<ApiResponseDTO<ChartSeriesDTO>> getTicketPriorityStatus();
}
