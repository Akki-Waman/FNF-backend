package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.request.TicketSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedTicketResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.TicketCombinedResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TicketService {
    ApiResponseDTO<CombinedTicketResponseDto> addTickets(Long ticketId, String ticketRequestDto, List<MultipartFile> multipartFile);

    ApiResponseDTO<Void> deleteTickets(DeleteTicketsRequestDTO requestDTO);

    ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>> searchTickets(
            TicketSearchRequestDto requestDto
    );

}
