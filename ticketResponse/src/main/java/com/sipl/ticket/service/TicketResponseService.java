package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResponseCombinedDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TicketResponseService {
    ApiResponseDTO<TicketResponseCombinedDto> addTicketResponse(String ticketResponseRequestDto, List<MultipartFile> files);


}
