package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.*;
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

    ApiResponseDTO<CombinedTicketNoteResponseDto> getByTicketId(Long ticketId);

    ApiResponseDTO<TicketCombinedResponseDto> updateTicketStatus(TicketStatusRequestDTO ticketStatusRequestDTO);

    ApiResponseDTO<String> requestTicketApproval(ApprovalRequestDTO dto);

    ApiResponseDTO<TicketCustomResponseDto> getAllTicketCustomDetails();
}
