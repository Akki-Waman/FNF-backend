package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.TicketNoteResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/ticket-notes")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Ticket Notes APIs")
public interface TicketNoteController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<TicketNoteResponseDTO>> saveTicketNotes(
            @Valid @RequestBody TicketNoteRequestDTO ticketNoteRequestDTO
    );
}

