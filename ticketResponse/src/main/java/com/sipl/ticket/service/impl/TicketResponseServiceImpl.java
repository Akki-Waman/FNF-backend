package com.sipl.ticket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.enums.NotificationPriority;
import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.TicketResponseRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResponseCombinedDto;
import com.sipl.ticket.core.mapper.TicketResponseAttachmentMapper;
import com.sipl.ticket.core.mapper.TicketResponseCcMapper;
import com.sipl.ticket.core.mapper.TicketResponseMapper;
import com.sipl.ticket.core.util.EmailUtil;
import com.sipl.ticket.service.TicketResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
import java.util.*;
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
    private final EmailUtil emailUtil;
    private final SettingRepository settingRepository;

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
            TicketResponse fullTicketResponse = ticketResponseRepository.findByIdWithAllDetails(ticketResponse.getTicketResponseId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found after save"));
            sendTicketResponseEmail(fullTicketResponse);


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


    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void sendTicketResponseEmail(TicketResponse ticketResponse) {

        Ticket ticket = ticketResponse.getTicket();

        List<String> toEmails = new ArrayList<>();
        List<String> ccEmails = Collections.emptyList();

        String subject = "N/A";
        String body = "N/A";
        String senderEmail = null;
        boolean emailSent = false;

        try {
            EmailNotificationRequest emailRequest = new EmailNotificationRequest();

            // ---------------- TO ----------------
            Optional.ofNullable(ticket.getAssignedTo())
                    .map(Users::getEmailId)
                    .ifPresent(toEmails::add);

            Optional.ofNullable(ticket.getEmailAddress())
                    .ifPresent(toEmails::add);

            emailRequest.setTo(toEmails);

            // ---------------- CC ----------------
            ccEmails = ticketResponseCcRepository
                    .findCcEmailsByTicketResponseId(
                            ticketResponse.getTicketResponseId()
                    );

            log.info("Ticket Response CC emails: {}", ccEmails);
            emailRequest.setCc(ccEmails);

            // ---------------- SUBJECT ----------------
            subject = "Ticket Updated | Ticket ID : " + ticket.getTicketId();
            emailRequest.setSubject(subject);

            // ---------------- BODY ----------------
            body = buildEmailBody(ticket, ticketResponse);
            emailRequest.setBody(body);

            // ---------------- SENDER ----------------
            senderEmail = settingRepository.findByScreen("EMAIL")
                    .map(Setting::getSettingValue)
                    .orElseThrow(() ->
                            new IllegalStateException("Sender email not configured"));

            emailRequest.setSender(senderEmail);
            emailRequest.setPriority(NotificationPriority.DEFAULT);

            // ---------------- SEND ----------------
            emailUtil.sendEmail(
                    emailRequest,
                    "TICKET_RESPONSE_" + ticketResponse.getTicketResponseId()
            );

            emailSent = true;

        } catch (Exception e) {
            log.error("Failed to send ticket response email. ticketResponseId={}",
                    ticketResponse.getTicketResponseId(), e);

        } finally {
            emailUtil.saveEmailLog(
                    toEmails,
                    subject,
                    body,
                    senderEmail,
                    emailSent ? "SUCCESS" : "FAILED",
                    emailSent ? "Email sent successfully" : "Email sending failed"
            );
        }
    }


    private String buildEmailBody(Ticket ticket, TicketResponse ticketResponse) {
        return String.format(
                "Dear Customer,\n\n" +
                        "Thank you for your patience.\n\n" +
                        "We would like to inform you that there has been an update on your support ticket.\n\n" +
                        "--------------------------------------------------\n" +
                        "Ticket ID     : %s\n" +
                        "Issue Subject : %s\n" +
                        "Response Type : %s\n" +
                        "Status        : %s → %s\n\n" +
                        "Response:\n" +
                        "%s\n" +
                        "--------------------------------------------------\n\n" +
                        "If you require any further assistance, please reply to this email.\n\n" +
                        "Warm Regards,\n" +
                        "Ticket Management System\n" +
                        "IT Support Team",
                ticket.getTicketId(),
                ticket.getSubject(),
                ticketResponse.getResponseType(),
                ticketResponse.getStatusBefore(),
                ticketResponse.getStatusAfter(),
                ticketResponse.getResponseBody()
        );
    }

}

