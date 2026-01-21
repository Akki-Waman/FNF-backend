package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TicketNoteController;
import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.TicketNoteResponseDTO;
import com.sipl.ticket.service.TicketNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketNoteControllerImpl implements TicketNoteController {

    private final TicketNoteService ticketNoteService;

    @Override
    public ResponseEntity<ApiResponseDTO<TicketNoteResponseDTO>> saveTicketNotes(TicketNoteRequestDTO ticketNoteRequestDTO) {
        log.info("<<Start>> saveTicketNotes endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<TicketNoteResponseDTO>> response =
                ResponseEntity.ok(ticketNoteService.saveTicketNotes(ticketNoteRequestDTO));
        log.info("<<End>> saveTicketNotes endpoint called <<End>>");
        return response;
    }
}
