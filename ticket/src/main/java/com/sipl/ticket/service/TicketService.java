package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedTicketResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TicketService {
    ApiResponseDTO<CombinedTicketResponseDto> addTickets(Long ticketId, String ticketRequestDto, List<MultipartFile> multipartFile);
    ApiResponseDTO<Void> deleteTickets(List<Long> ticketIds);

}
