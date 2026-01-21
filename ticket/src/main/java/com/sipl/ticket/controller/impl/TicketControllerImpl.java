package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketController;
import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.request.TicketSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
                ticketService.addTickets(ticketRequestDto, multipartFile);
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
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> updateTickets(String ticketRequestDto, List<MultipartFile> multipartFile) {
        log.info("<<START>> updateTickets controller <<START>>");
        ApiResponseDTO<CombinedTicketResponseDto> apiResponse =
                ticketService.updateTickets(ticketRequestDto, multipartFile);
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

    @Override
    public ResponseEntity<Void> exportTickets(
            ExportSearchRequestDTO request,
            HttpServletResponse response
    ) {

        log.info("<<Start>> exportTickets endpoint called <<Start>>");

        ticketService.exportTickets(request, response);

        log.info("<<End>> exportTickets endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Long>> getAllTicketIds() {

        log.info("<<START>> getAllTicketIds controller");

        ApiResponseDTO<Long> response =
                ticketService.getAllTicketIds();

        log.info("<<END>> getAllTicketIds controller");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTicketNoteResponseDto>> getByTicketId(Long ticketId) {
        log.info("<<START>> getByTicketId controller");
        ApiResponseDTO<CombinedTicketNoteResponseDto> response =
                ticketService.getByTicketId(ticketId);
        log.info("<<END>> getByTicketId controller");

        return ResponseEntity.ok(response);    }


}
