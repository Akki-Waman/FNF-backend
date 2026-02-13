package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResolutionCombinedDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TicketResolutionService {

    ApiResponseDTO<TicketResolutionCombinedDto> addTicketResolution(
            String ticketResolutionRequestDto,
            List<MultipartFile> files
    );
}
