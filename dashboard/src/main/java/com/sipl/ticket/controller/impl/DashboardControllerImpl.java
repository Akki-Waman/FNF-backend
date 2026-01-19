package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.DashboardController;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DashboardControllerImpl implements DashboardController {
    private final DashboardService dashboardService;

    @Override
    public ResponseEntity<ApiResponseDTO<ChartSeriesDTO>> getTicketStatus() {
        log.info("<<START>> getTicketStatus controller <<START>>");
        ApiResponseDTO<ChartSeriesDTO> apiResponse =
                dashboardService.getTicketStatus();
        log.info("<<END>> getTicketStatus controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }
    @Override
    public ResponseEntity<ApiResponseDTO<ChartSeriesDTO>> getTicketPriorityStatus() {
        log.info("<<START>> getTicketPriorityStatus controller <<START>>");
        ApiResponseDTO<ChartSeriesDTO> apiResponse =
                dashboardService.getTicketPriorityStatus();
        log.info("<<END>> getTicketPriorityStatus controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }


}
