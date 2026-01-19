package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.DashboardController;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartDataResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<ChartDataResponseDTO>> getTicketStatus() {
        log.info("<<START>> getTicketStatus <<START>>");
        ApiResponseDTO<ChartDataResponseDTO> apiResponse =
                dashboardService.getTicketStatus();
        log.info("<<END>> getTicketStatus <<END>>");
        return ResponseEntity.ok(apiResponse);
    }
    @Override
    public ResponseEntity<ApiResponseDTO<ChartDataResponseDTO>> getTicketPriorityStatus() {
        log.info("<<START>> getTicketPriorityStatus <<START>>");
        ApiResponseDTO<ChartDataResponseDTO> apiResponse =
                dashboardService.getTicketPriorityStatus();
        log.info("<<END>> getTicketPriorityStatus <<END>>");
        return ResponseEntity.ok(apiResponse);
    }


}
