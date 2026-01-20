package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.TaskFilterRequestDTO;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    ApiResponseDTO<ChartDataResponseDTO> getTicketStatus();

    ApiResponseDTO<ChartDataResponseDTO> getTicketPriorityStatus();

    ApiResponseDTO<TaskResponseDTO> getCalendarTasks(TaskFilterRequestDTO dto);

    ApiResponseDTO<ChartDataResponseDTO> getTicketAssignee();
}
