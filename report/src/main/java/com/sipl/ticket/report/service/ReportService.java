package com.sipl.ticket.report.service;

import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>>
    searchResponsePenaltyReport(ResponsePenaltyRequestDTO requestDto);
}
