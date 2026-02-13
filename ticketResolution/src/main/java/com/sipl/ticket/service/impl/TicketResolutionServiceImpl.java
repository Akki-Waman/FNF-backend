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
import com.sipl.ticket.core.dto.request.TicketResolutionRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResolutionCombinedDto;
import com.sipl.ticket.core.mapper.TicketResolutionAttachmentMapper;
import com.sipl.ticket.core.mapper.TicketResolutionCcMapper;
import com.sipl.ticket.core.mapper.TicketResolutionMapper;
import com.sipl.ticket.core.util.EmailUtil;
import com.sipl.ticket.master.service.MasterService;
import com.sipl.ticket.service.TicketResolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketResolutionServiceImpl implements TicketResolutionService {

    private final TicketResolutionMapper ticketResolutionMapper;
    private final TicketResolutionAttachmentMapper ticketResolutionAttachmentMapper;
    private final TicketResolutionRepository ticketResolutionRepository;
    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;
    private final TicketResolutionCcMapper ticketResolutionCcMapper;
    private final TicketResolutionCcRepository ticketResolutionCcRepository;
    private final TicketResolutionAttachmentRepository ticketResolutionAttachmentRepository;

    private final DocumentClientService documentClientService;
    private final EmailUtil emailUtil;

    private final SettingRepository settingRepository;
    private final SlaProfileRepository slaProfileRepository;
    private final SlaRuleDetailsRepository slaRuleDetailsRepository;
    private final MasterService masterService;

    public static final Long SLA_TYPE_RESOLUTION_ID = 2L;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "ADD",
            module = "TICKET_RESOLUTION",
            description = "Ticket resolution {0} added successfully"
    )
    public ApiResponseDTO<TicketResolutionCombinedDto> addTicketResolution(
            String ticketResolutionRequestDto,
            List<MultipartFile> files) {

        try {
            // 1️⃣ Parse request DTO
            TicketResolutionRequestDTO dto =
                    objectMapper.readValue(
                            ticketResolutionRequestDto,
                            TicketResolutionRequestDTO.class
                    );

            // 2️⃣ Save main resolution
            TicketResolution ticketResolution =
                    saveTicketResolution(dto);

            // 3️⃣ Save CCs
            List<TicketResolutionCc> ccs =
                    saveTicketResolutionCcs(
                            dto.getCcEmails(),
                            ticketResolution
                    );

            // 4️⃣ Save attachments
            List<TicketResolutionAttachment> attachments =
                    saveTicketResolutionAttachments(
                            files,
                            ticketResolution
                    );

            // 5️⃣ Build combined DTO
            TicketResolutionCombinedDto responseDto =
                    new TicketResolutionCombinedDto(
                            ticketResolutionMapper.toDto(ticketResolution),
                            ticketResolutionCcMapper.mapToDtoList(ccs),
                            ticketResolutionAttachmentMapper.mapToDtoList(attachments)
                    );

            // 6️⃣ Fetch full resolution for email
            TicketResolution fullResolution =
                    ticketResolutionRepository
                            .findByIdWithAllDetails(
                                    ticketResolution.getTicketResolutionId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Ticket resolution not found after save"
                                    ));

            // 7️⃣ Send email
            sendTicketResolutionEmail(fullResolution);

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Ticket resolved successfully",
                    HttpStatus.CREATED,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in addTicketResolution", e);
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

    @ActivityLoggable(
            action = "CREATE",
            module = "TICKET_RESOLUTION",
            description = "Ticket resolution {0} created successfully"
    )
    private TicketResolution saveTicketResolution(
            TicketResolutionRequestDTO dto) {

        validateTicketResolutionRequest(dto);

        Map<Integer, String> statusMap =
                masterService.getTicketStatusMap();

        if (!statusMap.containsKey(dto.getStatus())) {
            throw new IllegalArgumentException(
                    "Invalid status code: " + dto.getStatus()
            );
        }

        MasterContext masterContext =
                new MasterContext(null, statusMap);

        TicketResolution ticketResolution =
                ticketResolutionMapper.toEntity(dto, masterContext);

        Ticket ticket = getTicket(dto.getTicket());

        ticketResolution.setTicket(ticket);
        ticketResolution.setIsPublic(Boolean.TRUE.equals(dto.getIsPublic()));

        Integer oldStatus = ticket.getStatus();
        ticketResolution.setStatusBefore(statusMap.get(oldStatus));

        // FINAL STATUS UPDATE
        ticket.setStatus(dto.getStatus());
        ticketRepository.save(ticket);
        log.info("ticket repo save =>>>");
        // SLA LOGIC
        applyResolutionSlaLogic(ticket);

        return ticketResolutionRepository.save(ticketResolution);
    }

    private void validateTicketResolutionRequest(
            TicketResolutionRequestDTO dto) {

        if (dto == null) {
            throw new RuntimeException(
                    "Ticket resolution request cannot be null"
            );
        }
        if (dto.getTicket() == null) {
            throw new RuntimeException("Ticket ID is required");
        }
        if (dto.getResolutionBody() == null ||
                dto.getResolutionBody().trim().isEmpty()) {
            throw new RuntimeException(
                    "Resolution body is required"
            );
        }
    }

    @Transactional
    private List<TicketResolutionCc> saveTicketResolutionCcs(
            List<String> emails,
            TicketResolution ticketResolution) {

        if (emails == null || emails.isEmpty()) {
            return Collections.emptyList();
        }

        List<TicketResolutionCc> ccs = new ArrayList<>();

        for (String email :
                emails.stream().distinct().collect(Collectors.toList())) {

            if (email == null || email.trim().isEmpty()) continue;

            TicketResolutionCc cc = new TicketResolutionCc();
            cc.setTicketResolution(ticketResolution);
            cc.setEmail(email.trim());
            ccs.add(cc);
        }

        return ticketResolutionCcRepository.saveAll(ccs);
    }

    @Transactional
    private List<TicketResolutionAttachment> saveTicketResolutionAttachments(
            List<MultipartFile> files,
            TicketResolution ticketResolution) {

        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<TicketResolutionAttachment> attachments =
                new ArrayList<>();

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) continue;

            // Upload to DMS
            DmsResponseDTO<?> response =
                    documentClientService.upload(
                            file,
                            "ticket-resolution/"
                    );

            DocumentDTO document =
                    objectMapper.convertValue(
                            response.getData(),
                            DocumentDTO.class
                    );

            DmsDocument dmsDocument = new DmsDocument();
            dmsDocument.setDocumentId(document.getDocumentId());

            TicketResolutionAttachment attachment =
                    new TicketResolutionAttachment();

            attachment.setTicketResolution(ticketResolution);
            attachment.setDmsDocument(dmsDocument);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setFileSizeKB(
                    (int) (file.getSize() / 1024)
            );
            attachment.setUploadedOn(LocalDateTime.now());

            attachments.add(attachment);
        }

        return ticketResolutionAttachmentRepository.saveAll(attachments);
    }

    private void applyResolutionSlaLogic(Ticket ticket) {

        log.info("applyResolutionSlaLogic method start ===>> {}",ticket.getBranch().getBranchId());
        if (ticket.getResolutionDateTime() != null) {
            return;
        }

        ticket.setResolutionDateTime(LocalDateTime.now());

        Integer branchId = ticket.getBranch().getBranchId();
        log.info("branch ID : {}",branchId);

        if (branchId == null) {
            clearResolutionSla(ticket);
            return;
        }

        Optional<SlaProfile> slaProfileOpt =
                slaProfileRepository.findActiveProfileByBranch(
                        branchId,
                        LocalDate.now()
                );

        if (slaProfileOpt.isEmpty()) {
            clearResolutionSla(ticket);
            return;
        }

        log.info("sla profile id {}",slaProfileOpt.get().getSlaProfileId());

        Optional<SlaRuleDetails> ruleOpt =
                slaRuleDetailsRepository.findActiveRule(
                        slaProfileOpt.get().getSlaProfileId(),
                        ticket.getService().getServiceId(),
                        ticket.getPriority(),
                        SLA_TYPE_RESOLUTION_ID
                );

        if (ruleOpt.isEmpty()) {
            clearResolutionSla(ticket);
            return;
        }
        log.info("sla rule details {}",ruleOpt.get().getSlaRuleDetailId());

        calculateResolutionSla(ticket, ruleOpt.get());
        ticketRepository.save(ticket);
    }

    private void calculateResolutionSla(
            Ticket ticket,
            SlaRuleDetails rule) {

        log.info("SLA rule detail id {}", rule.getSlaRuleDetailId());

        double slaHours = rule.getSlaHours();
        int graceMinutes = rule.getGraceHours() * 60;
        double penaltyPercentPerMinute =
                rule.getPenaltyPercent() / 60;

        double resolutionHours =
                Duration.between(
                        ticket.getCreatedTime(),
                        ticket.getResolutionDateTime()
                ).toMinutes() / 60.0;

        boolean withinSla = resolutionHours <= slaHours;

        log.info(
                "RESOLUTION SLA CALC → resolutionHours={}, slaHours={}, withinSla={}",
                resolutionHours, slaHours, withinSla
        );

        ticket.setResolutionSlaHours(slaHours);
        ticket.setResolutionTimeHours(resolutionHours);
        ticket.setResolutionWithinSla(withinSla);

        if (withinSla) {
            ticket.setPenaltyAllowed(false);
            ticket.setResolutionPenaltyTime(0.0);
            ticket.setResolutionPenaltyPercentage(BigDecimal.ZERO);
        } else {

            double penaltyMinutes =
                    Math.max(
                            0,
                            (resolutionHours * 60)
                                    - (slaHours * 60)
                                    - graceMinutes
                    );

            BigDecimal calculatedPenaltyPercentage =
                    BigDecimal.valueOf(penaltyMinutes)
                            .multiply(BigDecimal.valueOf(penaltyPercentPerMinute));

            // 🔒 MAX PENALTY CAP
            BigDecimal maxPenaltyPercent =
                    BigDecimal.valueOf(rule.getMaxPenaltyPercent());

            if (calculatedPenaltyPercentage.compareTo(maxPenaltyPercent) > 0) {
                calculatedPenaltyPercentage = maxPenaltyPercent;
            }

            ticket.setPenaltyAllowed(true);
            ticket.setResolutionPenaltyTime(penaltyMinutes);
            ticket.setResolutionPenaltyPercentage(calculatedPenaltyPercentage);
        }
    }


    private void clearResolutionSla(Ticket ticket) {
        ticket.setResolutionSlaHours(null);
        ticket.setResolutionTimeHours(null);
        ticket.setResolutionWithinSla(null);
        ticket.setPenaltyAllowed(null);
        ticket.setResolutionPenaltyTime(null);
        ticket.setResolutionPenaltyPercentage(null);
    }


    private Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ticket not found for id=" + ticketId
                        ));
    }

    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void sendTicketResolutionEmail(
            TicketResolution ticketResolution) {

        Ticket ticket = ticketResolution.getTicket();

        List<String> toEmails = new ArrayList<>();
        List<String> ccEmails = Collections.emptyList();

        String subject = "N/A";
        String body = "N/A";
        String senderEmail = null;
        boolean emailSent = false;

        try {
            EmailNotificationRequest emailRequest =
                    new EmailNotificationRequest();

            // ---------------- TO ----------------
            Optional.ofNullable(ticket.getAssignedTo())
                    .map(Users::getEmailId)
                    .ifPresent(toEmails::add);

            Optional.ofNullable(ticket.getEmailAddress())
                    .ifPresent(toEmails::add);

            emailRequest.setTo(toEmails);

            // ---------------- CC ----------------
            ccEmails =
                    ticketResolutionCcRepository
                            .findCcEmailsByTicketResolutionId(
                                    ticketResolution.getTicketResolutionId()
                            );

            log.info("Ticket Resolution CC emails: {}", ccEmails);
            emailRequest.setCc(ccEmails);

            // ---------------- SUBJECT ----------------
            subject =
                    "Ticket Resolved | Ticket ID : " +
                            ticket.getTicketId();

            emailRequest.setSubject(subject);

            // ---------------- BODY ----------------
            body = buildResolutionEmailBody(
                    ticket,
                    ticketResolution
            );

            emailRequest.setBody(body);

            // ---------------- SENDER ----------------
            senderEmail =
                    settingRepository.findByScreen("EMAIL")
                            .map(Setting::getSettingValue)
                            .orElseThrow(() ->
                                    new IllegalStateException(
                                            "Sender email not configured"));

            emailRequest.setSender(senderEmail);
            emailRequest.setPriority(NotificationPriority.DEFAULT);

            // ---------------- SEND ----------------
            emailUtil.sendEmail(
                    emailRequest,
                    "TICKET_RESOLUTION_" +
                            ticketResolution.getTicketResolutionId()
            );

            emailSent = true;

        } catch (Exception e) {
            log.error(
                    "Failed to send ticket resolution email. ticketResolutionId={}",
                    ticketResolution.getTicketResolutionId(),
                    e
            );

        } finally {
            emailUtil.saveEmailLog(
                    toEmails,
                    subject,
                    body,
                    senderEmail,
                    emailSent ? "SUCCESS" : "FAILED",
                    emailSent
                            ? "Email sent successfully"
                            : "Email sending failed"
            );
        }
    }

    private String buildResolutionEmailBody(
            Ticket ticket,
            TicketResolution ticketResolution) {

        return String.format(
                "Dear %s,\n\n" +
                        "Thank you for your patience.\n\n" +
                        "We would like to inform you that your support ticket has been resolved.\n\n" +
                        "--------------------------------------------------\n" +
                        "Ticket ID     : %s\n" +
                        "Issue Subject : %s\n" +
                        "Resolution:\n" +
                        "%s\n" +
                        "--------------------------------------------------\n\n" +
                        "If you require any further assistance, please reply to this email.\n\n" +
                        "Warm Regards,\n" +
                        "Ticket Management System\n" +
                        "IT Support Team",
                ticket.getComplaintName(),
                ticket.getTicketId(),
                ticket.getSubject(),
                ticketResolution.getResolutionBody()
        );
    }

}

