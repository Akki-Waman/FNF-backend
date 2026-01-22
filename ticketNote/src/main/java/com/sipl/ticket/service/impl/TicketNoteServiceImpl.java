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

    @Override
    public ApiResponseDTO<TicketNoteResponseDTO> updateTicketNotes(TicketNoteRequestDTO dto) {
        log.info("Update ticket note request received. ticketNoteId={}, ticketId={}",
                dto.getTicketNoteId(), dto.getTicketId());
        try {
            if (dto.getTicketNoteId() == null) {
                log.info("Ticket note update failed: ticketNoteId is null");
                return new ApiResponseDTO<>(
                        null,
                        "Ticket note id is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            log.info("Fetching TicketNote with id={}", dto.getTicketNoteId());
            TicketNote ticketNote = ticketNoteRepository
                    .findById(dto.getTicketNoteId())
                    .orElseThrow(() -> {
                        log.info("TicketNote not found for id={}", dto.getTicketNoteId());
                        return new RuntimeException("Ticket note not found");
                    });
            if (dto.getTicketId() != null) {
                log.info("Validating Ticket with id={}", dto.getTicketId());
                Ticket ticket = ticketRepository
                        .findById(dto.getTicketId())
                        .orElseThrow(() -> {
                            log.info("Ticket not found for id={}", dto.getTicketId());
                            return new RuntimeException("Ticket not found");
                        });
                ticketNote.setTicket(ticket);
            }
            log.info("Updating TicketNote fields. isDeleted={}, notesLength={}",
                    dto.getIsDeleted(),
                    dto.getNotes() != null ? dto.getNotes().length() : 0);
            ticketNote.setNotes(dto.getNotes());
            ticketNote.setIsDeleted(
                    dto.getIsDeleted() != null ? dto.getIsDeleted() : false
            );
            log.info("Saving updated TicketNote id={}", ticketNote.getTicketNoteId());
            TicketNote savedNote = ticketNoteRepository.save(ticketNote);
            TicketNoteResponseDTO responseDTO = ticketNoteMapper.toDto(savedNote);
            log.info("Ticket note updated successfully. ticketNoteId={}",
                    savedNote.getTicketNoteId());
            return new ApiResponseDTO<>(
                    responseDTO,
                    "Ticket note updated successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.info("Error occurred while updating ticket note. ticketNoteId={}",
                    dto.getTicketNoteId(), e);
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<TicketNoteResponseDTO> getByTicketId(Long ticketId) {
        log.info("Get ticket note by ticketId request received. ticketId={}", ticketId);
        try {
            log.info("Fetching TicketNote for ticketId={}", ticketId);
            TicketNote ticketNote = ticketNoteRepository
                    .findByTicket_TicketIdAndIsDeletedFalse(ticketId)
                    .orElseThrow(() -> {
                        log.info("TicketNote not found for ticketId={}", ticketId);
                        return new RuntimeException("Ticket note not found");
                    });
            TicketNoteResponseDTO responseDTO = ticketNoteMapper.toDto(ticketNote);
            log.info("Ticket note fetched successfully for ticketId={}", ticketId);
            return new ApiResponseDTO<>(
                    responseDTO,
                    "Ticket note fetched successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.error("Error occurred while fetching ticket note. ticketId={}", ticketId, e);
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<String> deleteByTicketNoteId(Long ticketNoteId) {
        log.info("Delete ticket note request received. ticketNoteId={}", ticketNoteId);
        try {
            log.info("Fetching TicketNote for delete. ticketNoteId={}", ticketNoteId);
            TicketNote ticketNote = ticketNoteRepository
                    .findById(ticketNoteId)
                    .orElseThrow(() -> {
                        log.info("TicketNote not found for delete. ticketNoteId={}", ticketNoteId);
                        return new RuntimeException("Ticket note not found");
                    });
            if (Boolean.TRUE.equals(ticketNote.getIsDeleted())) {
                log.info("TicketNote already deleted. ticketNoteId={}", ticketNoteId);
                return new ApiResponseDTO<>(
                        null,
                        "Ticket note already deleted",
                        HttpStatus.OK,
                        false
                );
            }
            ticketNote.setIsDeleted(true);
            log.info("Soft deleting TicketNote id={}", ticketNoteId);
            ticketNoteRepository.save(ticketNote);
            log.info("Ticket note deleted successfully. ticketNoteId={}", ticketNoteId);
            return new ApiResponseDTO<>(
                    null,
                    "Ticket note deleted successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.info("Error occurred while deleting ticket note. ticketNoteId={}", ticketNoteId, e);
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }



}
