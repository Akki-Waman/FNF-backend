package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketResolutionController;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResolutionCombinedDto;
import com.sipl.ticket.service.TicketResolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketResolutionControllerImpl
        implements TicketResolutionController {

    private final TicketResolutionService ticketResolutionService;

    @Override
    public ResponseEntity<ApiResponseDTO<TicketResolutionCombinedDto>>
    addTicketResolution(String ticketResolutionRequestDto,
                        List<MultipartFile> files) {

        log.info("<<START>> addTicketResolution <<START>>");

        ApiResponseDTO<TicketResolutionCombinedDto> response =
                ticketResolutionService.addTicketResolution(
                        ticketResolutionRequestDto, files
                );

        log.info("<<END>> addTicketResolution <<END>>");
        return ResponseEntity.ok(response);
    }

}
