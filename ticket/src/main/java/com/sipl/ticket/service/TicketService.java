package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.request.TicketSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface TicketService {
    ApiResponseDTO<CombinedTicketResponseDto> addTickets(String ticketRequestDto, List<MultipartFile> multipartFile);

    ApiResponseDTO<Void> deleteTickets(DeleteTicketsRequestDTO requestDTO);

    ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>> searchTickets(
            TicketSearchRequestDto requestDto
    );

    ApiResponseDTO<CombinedTicketResponseDto> updateTickets( String ticketRequestDto, List<MultipartFile> multipartFile);

    ApiResponseDTO<SummaryKpiResponseDTO> getTikctSummary();

    void exportTickets(
            ExportSearchRequestDTO request,
            HttpServletResponse response
    );

    ApiResponseDTO<Long> getAllTicketIds();

    ApiResponseDTO<CombinedTicketResponseDto> getByTicketId(Long ticketId);
}
