package com.sipl.ticket.service.impl;

import org.springframework.http.HttpStatus;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketNote;
import com.sipl.ticket.core.dao.repository.TicketNoteRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dto.request.TicketNoteRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketNoteResponseDTO;
import com.sipl.ticket.core.mapper.TicketNoteMapper;
import com.sipl.ticket.service.TicketNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketNoteServiceImpl implements TicketNoteService {

    private final TicketNoteRepository ticketNoteRepository;
    private final TicketNoteMapper ticketNoteMapper;
    private final TicketRepository ticketRepository;

    @Override
    public ApiResponseDTO<TicketNoteResponseDTO> saveTicketNotes(
            TicketNoteRequestDTO ticketNoteRequestDTO) {
        log.info("Saving ticket note for ticketId: {}", ticketNoteRequestDTO.getTicketId());
        try {
            Ticket ticket = ticketRepository
                    .findById(ticketNoteRequestDTO.getTicketId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));
            TicketNote ticketNotes = new TicketNote();
            ticketNotes.setNotes(ticketNoteRequestDTO.getNotes());
            ticketNotes.setIsDeleted(false);
            ticketNotes.setTicket(ticket);
            TicketNote savedEntity = ticketNoteRepository.save(ticketNotes);
            TicketNoteResponseDTO response = ticketNoteMapper.toDto(savedEntity);
            return new ApiResponseDTO<>(
                    response,
                    "Ticket note created successfully",
                    HttpStatus.CREATED,
                    false
            );
        } catch (Exception e) {
            log.error("Error occurred while saving ticket note", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
