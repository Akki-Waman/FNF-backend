package com.sipl.ticket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dao.repository.TicketResponseAttachmentRepository;
import com.sipl.ticket.core.dao.repository.TicketResponseCcRepository;
import com.sipl.ticket.core.dao.repository.TicketResponseRepository;
import com.sipl.ticket.core.dto.request.TicketResponseRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResponseCombinedDto;
import com.sipl.ticket.core.mapper.TicketResponseAttachmentMapper;
import com.sipl.ticket.core.mapper.TicketResponseCcMapper;
import com.sipl.ticket.core.mapper.TicketResponseMapper;
import com.sipl.ticket.service.TicketResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketResponseServiceImpl implements TicketResponseService {

    private final TicketResponseMapper ticketResponseMapper;
    private final TicketResponseRepository ticketResponseRepository;
    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;
    private final TicketResponseCcMapper ticketResponseCcMapper;
    private final TicketResponseAttachmentMapper ticketResponseAttachmentMapper;
    private final DocumentClientService documentClientService;
    private final TicketResponseCcRepository ticketResponseCcRepository;
    private final TicketResponseAttachmentRepository ticketResponseAttachmentRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(action = "ADD", module = "TICKET_RESPONSE")
    public ApiResponseDTO<TicketResponseCombinedDto> addTicketResponse(
            String ticketResponseRequestDto,
            List<MultipartFile> files) {

        try {
            // 1️⃣ Parse DTO
            TicketResponseRequestDTO dto =
                    objectMapper.readValue(ticketResponseRequestDto, TicketResponseRequestDTO.class);

            // 2️⃣ Validate and save main TicketResponse
            TicketResponse ticketResponse = saveTicketResponse(dto);
            log.info("ticketResponse");
            // 3️⃣ Save CC emails (new rows)
            List<TicketResponseCc> ccs =
                    saveTicketResponseCcs(dto.getCcEmails(), ticketResponse);
            log.info("saveTicketResponseCcs");

            // 4️⃣ Save attachments (DMS upload)
            List<TicketResponseAttachment> attachments =
                    saveTicketResponseAttachments(files, ticketResponse);
            log.info("saveTicketResponseAttachments");

            // 5️⃣ Build combined response DTO
            TicketResponseCombinedDto responseDto = new TicketResponseCombinedDto(
                    ticketResponseMapper.toDto(ticketResponse),
                    ticketResponseCcMapper.mapToDtoList(ccs),
                    ticketResponseAttachmentMapper.mapToDtoList(attachments)
            );

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Ticket response added successfully",
                    HttpStatus.CREATED,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in addTicketResponse service", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @ActivityLoggable(action = "CREATE", module = "TICKET_RESPONSE")
    private TicketResponse saveTicketResponse(TicketResponseRequestDTO dto) {

        validateTicketResponseRequest(dto);

        Ticket ticket = getTicket(dto.getTicket());

        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setTicket(ticket);
        ticketResponse.setResponseBody(dto.getResponseBody());
        ticketResponse.setResponseType(dto.getResponseType());
        ticketResponse.setIsPublic(Boolean.TRUE.equals(dto.getIsPublic()));
        ticketResponse.setStatusBefore(dto.getStatusBefore());
        ticketResponse.setStatusAfter(dto.getStatusAfter());

        TicketResponse saved = ticketResponseRepository.save(ticketResponse);
        ticketResponseRepository.flush();

        log.info("TicketResponse saved with id={}", saved.getTicketResponseId());

        return saved;
    }

    private void validateTicketResponseRequest(TicketResponseRequestDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Ticket response request cannot be null");
        }
        if (dto.getTicket() == null) {
            throw new RuntimeException("Ticket ID is required");
        }
        if (dto.getResponseBody() == null || dto.getResponseBody().trim().isEmpty()) {
            throw new RuntimeException("Response body is required");
        }
        if (dto.getResponseType() == null || dto.getResponseType().trim().isEmpty()) {
            throw new RuntimeException("Response type is required");
        }
    }

    // Save CC emails (new rows)
    @Transactional
    private List<TicketResponseCc> saveTicketResponseCcs(
            List<String> emails,
            TicketResponse ticketResponse) {

        if (emails == null || emails.isEmpty()) {
            log.info("No CC emails provided for ticketResponseId={}", ticketResponse.getTicketResponseId());
            return Collections.emptyList();
        }

        List<TicketResponseCc> ccs = new ArrayList<>();

        for (String email : emails.stream().distinct().collect(Collectors.toList())) {
            if (email == null || email.trim().isEmpty()) continue;

            TicketResponseCc cc = new TicketResponseCc();
            cc.setTicketResponse(ticketResponse); // FK-safe parent
            cc.setEmail(email.trim());
            ccs.add(cc);
        }

        log.info("Saving {} CCs for ticketResponseId={}", ccs.size(), ticketResponse.getTicketResponseId());
        return ticketResponseCcRepository.saveAll(ccs);
    }

    @Transactional
    private List<TicketResponseAttachment> saveTicketResponseAttachments(
            List<MultipartFile> files,
            TicketResponse ticketResponse) {

        if (files == null || files.isEmpty()) {
            log.info("No files provided for ticketResponseId={}", ticketResponse.getTicketResponseId());
            return Collections.emptyList();
        }

        List<TicketResponseAttachment> attachments = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                log.warn("Empty file skipped for ticketResponseId={}", ticketResponse.getTicketResponseId());
                continue;
            }

            log.info("Uploading file={} for ticketResponseId={}", file.getOriginalFilename(), ticketResponse.getTicketResponseId());

            // 1️⃣ Upload to DMS
            DmsResponseDTO<?> response = documentClientService.upload(file, "ticket-response/");
            DocumentDTO document = objectMapper.convertValue(response.getData(), DocumentDTO.class);

            // 2️⃣ Map DMS document
            DmsDocument dmsDocument = new DmsDocument();
            dmsDocument.setDocumentId(document.getDocumentId());

            // 3️⃣ Map attachment entity
            TicketResponseAttachment attachment = new TicketResponseAttachment();
            attachment.setTicketResponse(ticketResponse); // FK-safe parent
            attachment.setDmsDocument(dmsDocument);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setFileSizeKB((int) (file.getSize() / 1024));
            attachment.setUploadedOn(LocalDateTime.now());

            attachments.add(attachment);
        }

        log.info("Saving {} attachments for ticketResponseId={}", attachments.size(), ticketResponse.getTicketResponseId());
        return ticketResponseAttachmentRepository.saveAll(attachments);
    }


    // Get ticket (validate exists)
    private Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id=" + ticketId));
    }



}

