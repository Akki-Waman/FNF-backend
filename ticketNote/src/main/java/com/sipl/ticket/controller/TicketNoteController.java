package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
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

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<TicketNoteResponseDTO>> updateTicketNotes(
            @RequestBody TicketNoteRequestDTO ticketNoteRequestDTO
    );

    @GetMapping("/get/{ticketId}")
    ResponseEntity<ApiResponseDTO<TicketNoteResponseDTO>> getByTicketId(
            @PathVariable Long ticketId
    );

    @DeleteMapping("/delete/{ticketNoteId}")
    ResponseEntity<ApiResponseDTO<String>> deleteByTicketNoteId(
            @PathVariable Long ticketNoteId
    );
}

