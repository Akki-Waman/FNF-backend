package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketResponseController;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedTaskResponseDto;
import com.sipl.ticket.core.dto.response.TicketResponseCombinedDto;
import com.sipl.ticket.service.TicketResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketResponseControllerImpl implements TicketResponseController {

    private final TicketResponseService ticketResponseService;

    @Override
    public ResponseEntity<ApiResponseDTO<TicketResponseCombinedDto>> addTicketResponse(String ticketResponseRequestDto, List<MultipartFile> files) {
        log.info("<<START>> addTicketResponse <<START>>");
        ApiResponseDTO<TicketResponseCombinedDto> response =
                ticketResponseService.addTicketResponse(ticketResponseRequestDto, files);
        log.info("<<END>> addTicketResponse <<END>>");
        return ResponseEntity.ok(response);    }
}
