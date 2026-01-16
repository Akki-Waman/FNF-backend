package com.sipl.ticket.report.controller.impl;

import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import com.sipl.ticket.report.controller.ReportController;
import com.sipl.ticket.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportControllerImpl implements ReportController {

    private final ReportService reportService;

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>>> searchResponsePenaltyReport(ResponsePenaltyRequestDTO requestDto) {
        log.info("<<START>> searchResponsePenaltyReport <<START>>");

        ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>> response =
                reportService.searchResponsePenaltyReport(requestDto);
        log.info("<<END>> searchResponsePenaltyReport <<END>>");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
