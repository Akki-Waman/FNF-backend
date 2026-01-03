package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketController;
import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import com.sipl.ticket.core.dto.response.CombinedTicketResponseDto;
import com.sipl.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketControllerImpl implements TicketController {
    private final TicketService ticketService;

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> addTickets(String ticketRequestDto, List<MultipartFile> multipartFile) {
        log.info("<<START>> addProduct controller <<START>>");
        ApiResponseDTO<CombinedTicketResponseDto> apiResponse =
                ticketService.addTickets(null,ticketRequestDto, multipartFile);
        log.info("<<END>> addProduct controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }
    @Override
    public ResponseEntity<ApiResponseDTO<Void>> deleteTickets(
            DeleteTicketsRequestDTO requestDTO) {

        log.info("<<START>> deleteTickets controller");

        ApiResponseDTO<Void> response =
                ticketService.deleteTickets(requestDTO.getTicketIds());

        log.info("<<END>> deleteTickets controller");
        return ResponseEntity.ok(response);
    }

}
