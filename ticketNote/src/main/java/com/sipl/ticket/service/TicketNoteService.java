package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketNoteResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TicketNoteService {
    public ApiResponseDTO<TicketNoteResponseDTO> saveTicketNotes(TicketNoteRequestDTO ticketNoteRequestDTO);
}
