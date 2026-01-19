package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    ApiResponseDTO<ChartSeriesDTO> getTicketStatus();

    ApiResponseDTO<ChartSeriesDTO> getTicketPriorityStatus();
}
