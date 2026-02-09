package com.sipl.ticket.report.service;

import com.sipl.ticket.core.dto.request.ActivityLogReportRequestDto;
import com.sipl.ticket.core.dto.request.ResolutionPenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.StaffTicketRequestDTO;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ReportService {

    ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>>
    searchResponsePenaltyReport(ResponsePenaltyRequestDTO requestDto);

    ApiResponseDTO<PagedResponse<StaffTicketResponseDTO>> staffTicketReport(StaffTicketRequestDTO requestDto);

    void exportResponsePenaltyReport(
            ResponsePenaltyRequestDTO requestDto,
            String format,
            HttpServletResponse response
    );

    ApiResponseDTO<PagedResponse<ResolutionPenaltyResponseDTO>>
    searchResolutionPenaltyReport(ResolutionPenaltyRequestDTO requestDto);

    void exportResolutionPenaltyReport(
            ResolutionPenaltyRequestDTO requestDto,
            String format,
            HttpServletResponse response
    );

    ApiResponseDTO<PagedResponse<ActivityLogReportResponseDto>>
    searchActivityLogReport(ActivityLogReportRequestDto requestDto);

}
