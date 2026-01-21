package com.sipl.ticket.report.controller.impl;

import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.StaffTicketRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import com.sipl.ticket.core.dto.response.StaffTicketResponseDTO;
import com.sipl.ticket.report.controller.ReportController;
import com.sipl.ticket.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<StaffTicketResponseDTO>>> staffTicketReport(StaffTicketRequestDTO requestDto) {
        log.info("<<START>> staffTicketReport <<START>>");

        ApiResponseDTO<PagedResponse<StaffTicketResponseDTO>> response =
                reportService.staffTicketReport(requestDto);
        log.info("<<END>> staffTicketReport <<END>>");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);    }

    @Override
    public ResponseEntity<Void> exportResponsePenaltyReport(
            ResponsePenaltyRequestDTO requestDto,
            String format,
            HttpServletResponse response
    ) {
        log.info("<<START>> exportResponsePenaltyReport <<START>>");

        reportService.exportResponsePenaltyReport(requestDto, format, response);

        log.info("<<END>> exportResponsePenaltyReport <<END>>");

        return ResponseEntity.ok().build();
    }

}
