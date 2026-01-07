package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketController;
import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.request.TicketSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
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
                ticketService.deleteTickets(requestDTO);

        log.info("<<END>> deleteTickets controller");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>>> searchTickets(
            TicketSearchRequestDto requestDto) {

        log.info("<<Start>> searchTickets <<Start>>");

        ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>> response =
                ticketService.searchTickets(requestDto);

        log.info("<<End>> searchTickets <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> updateTickets(Long ticketId, String ticketRequestDto, List<MultipartFile> multipartFile) {
        log.info("<<START>> updateTickets controller <<START>>");
        ApiResponseDTO<CombinedTicketResponseDto> apiResponse =
                ticketService.updateTickets(ticketId,ticketRequestDto, multipartFile);
        log.info("<<END>> updateTickets controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SummaryKpiResponseDTO>> getTikctSummary() {
        log.info("<<START>> getTikctSummary controller <<START>>");
        ApiResponseDTO<SummaryKpiResponseDTO> apiResponse =
                ticketService.getTikctSummary();
        log.info("<<END>> getTikctSummary controller <<END>>");
        return ResponseEntity.ok(apiResponse);    }
}
