package com.sipl.ticket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.enums.NotificationPriority;
import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.*;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.helper.TicketExcelExportHelper;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.core.util.EmailUtil;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.core.util.TicketFileUploadUtil;
import com.sipl.ticket.master.service.MasterService;
import com.sipl.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final ObjectMapper objectMapper;
    private final TagsRepository tagsRepository;
    private final TicketTagMapper ticketTagMapper;
    private final TicketTagRepository ticketTagRepository;
    private final TicketCcRepository ticketCcRepository;
    private final TicketAttachmentRepository ticketAttachmentRepository;
    private final TicketCcMapper ticketCcMapper;
    private final TicketAttachmentMapper ticketAttachmentMapper;
    private final TicketFileUploadUtil fileUploadUtil;
    private final UsersRepository userRepository;
    private final LocationRepository locationRepository;
    private final ContactRepository contactsRepository;
    private final DepartmentRepository departmentRepository;
    private final ClientProductsRepository clientProductsRepository;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchesRepository;
    private final DocumentClientService documentClientService;
    private final EmailUtil emailUtil;
    private final SettingRepository settingRepository;
    private final MasterService masterService;
    private final TicketResponseRepository ticketResponseRepository;
    private final MastersRepository mastersRepository;

    @Override
        @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "CREATE",
            module = "TICKET",
            description = "Ticket {0} created successfully"
    )
        public ApiResponseDTO<CombinedTicketResponseDto> addTickets(
                Long ticketId,
                String ticketRequestDto,
                List<MultipartFile> multipartFiles) {
            log.info("<<START>> addTickets service");
            try {
                NewTicketsRequestDTO requestDto =
                        objectMapper.readValue(ticketRequestDto, NewTicketsRequestDTO.class);
                TicketsResponseDTO ticketDto = requestDto.getTicketsResponseDTO();
                Users assignedUser = validateAndGetAssignedUser(ticketDto);
                Ticket savedTicket = saveTicket(ticketDto, assignedUser);
                List<TicketTag> ticketTags =
                        saveTicketTags(requestDto.getTicketTagResponseDTOS(), savedTicket);
                List<TicketCc> ticketCcs =
                        saveTicketCcs(requestDto.getTicketCcResponseDTOS(), savedTicket);
                List<TicketAttachment> attachments =
                        saveTicketAttachments(multipartFiles, savedTicket, assignedUser);
                Ticket fullTicket = ticketRepository.findByIdWithAllDetails(savedTicket.getTicketId())
                        .orElseThrow(() -> new RuntimeException("Ticket not found after save"));

                sendTicketCreationEmail(fullTicket);

                CombinedTicketResponseDto responseDto = buildResponse(
                        fullTicket, ticketTags, ticketCcs, attachments
                );
                log.info("<<END>> addTickets service SUCCESS");
                return new ApiResponseDTO<>(
                        responseDto, null, null,
                        "Ticket created successfully",
                        HttpStatus.CREATED, false, null, null
                );
            } catch (Exception e) {
                log.error("Exception in addTickets service:", e);
                return new ApiResponseDTO<>(
                        null, null, null,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        true, null, null
                );
            }
        }

        @Transactional
        private Ticket saveTicket(TicketsResponseDTO dto, Users assignedUser) {
            Ticket ticket = ticketMapper.toEntity(dto);
            ticket.setAssignedTo(assignedUser);
            ticket.setLocation(getLocation(dto));
            ticket.setContact(getContact(dto));
            ticket.setDepartment(getDepartment(dto));
            ticket.setService(getService(dto));
            ticket.setClientProducts(getClientProduct(dto));
            ticket.setBranch(getBranch(dto));
            ticket.setIsDeleted(false);
            Ticket savedTicket = ticketRepository.save(ticket);
            log.info("Ticket saved with ID: {}", savedTicket.getTicketId());
            return savedTicket;
        }

        @Transactional
        private List<TicketTag> saveTicketTags(
                List<TicketTagResponseDTO> tagDtos,
                Ticket ticket) {
            if (tagDtos == null || tagDtos.isEmpty()) return Collections.emptyList();
            List<TicketTag> tags = tagDtos.stream()
                    .filter(dto -> dto.getTags() != null && dto.getTags().getTagId() != null)
                    .map(dto -> {
                        Tags tag = tagsRepository.findById(dto.getTags().getTagId())
                                .orElseThrow(() ->
                                        new RuntimeException("Tag not found with id " + dto.getTags().getTagId()));
                        return ticketTagMapper.toEntity(dto, ticket, tag);
                    })
                    .collect(Collectors.toList());
            return ticketTagRepository.saveAll(tags);
        }

        @Transactional
        private List<TicketCc> saveTicketCcs(
                List<TicketCcResponseDTO> ccDtos,
                Ticket ticket) {
            if (ccDtos == null || ccDtos.isEmpty()) return Collections.emptyList();
            List<TicketCc> ccs = ccDtos.stream()
                    .filter(dto -> dto.getCc() != null && !dto.getCc().isBlank())
                    .map(dto -> ticketCcMapper.toEntity(dto, ticket))
                    .collect(Collectors.toList());
            return ticketCcRepository.saveAll(ccs);
        }

    @Transactional
    private List<TicketAttachment> saveTicketAttachments(
            List<MultipartFile> files,
            Ticket ticket,
            Users uploadedBy) {
        try {
            if (files == null || files.isEmpty()) {
                log.info("No files found for ticketId={}", ticket.getTicketId());
                return Collections.emptyList();
            }
            List<TicketAttachment> attachments = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    log.warn("Empty file skipped for ticketId={}", ticket.getTicketId());
                    continue;
                }
                log.info("Uploading file={} for ticketId={}",
                        file.getOriginalFilename(), ticket.getTicketId());

                DmsResponseDTO<?> response = documentClientService.upload(file, "tickets/");
                log.info("response: " + response.toString());
                Object data = response.getData();
                log.info("data: " + data);
                DocumentDTO document;
                if (data instanceof Map) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    document = mapper.convertValue(data, DocumentDTO.class);
                } else {
                    document = (DocumentDTO) data;
                }
                Long documentId = document.getDocumentId();
                TicketAttachmentResponseDTO dto = new TicketAttachmentResponseDTO();
                dto.setFileName(file.getOriginalFilename());
                dto.setFileSizeKB((int) (file.getSize() / 1024));
                dto.setContentType(file.getContentType());

                TicketAttachment attachment =
                        ticketAttachmentMapper.toEntity(dto, ticket, uploadedBy);
                DmsDocument dmsDocument = new DmsDocument();
                dmsDocument.setDocumentId(documentId);
                attachment.setDmsDocument(dmsDocument);

                attachments.add(attachment);
            }
            log.info("Saving {} attachments for ticketId={}",
                    attachments.size(), ticket.getTicketId());
            return ticketAttachmentRepository.saveAll(attachments);
        } catch (Exception e) {
            log.error("Error while saving ticket attachments for ticketId={}",
                    ticket != null ? ticket.getTicketId() : null, e);
            throw e;
        }
    }

    private Users validateAndGetAssignedUser(TicketsResponseDTO dto) {
            if (dto.getAssignedTo() == null || dto.getAssignedTo().getId() == null) {
                throw new RuntimeException("AssignedTo user is required");
            }
            return userRepository.findById(dto.getAssignedTo().getId())
                    .orElseThrow(() ->
                            new RuntimeException("AssignedTo user not found"));
        }

        private Locations getLocation(TicketsResponseDTO dto) {
            return locationRepository.findById(dto.getLocation().getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
        }

        private Contact getContact(TicketsResponseDTO dto) {
            return contactsRepository.findById(dto.getContact().getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found"));
        }

        private Department getDepartment(TicketsResponseDTO dto) {
            return departmentRepository.findById(dto.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }

        private ServiceEntity getService(TicketsResponseDTO dto) {
            return serviceRepository.findById(dto.getService().getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
        }

        private ClientProducts getClientProduct(TicketsResponseDTO dto) {
            return clientProductsRepository.findById(dto.getClientProducts().getClientProductId())
                    .orElseThrow(() -> new RuntimeException("Client Product not found"));
        }

        private Branches getBranch(TicketsResponseDTO dto) {
            return branchesRepository.findById(dto.getBranch().getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

    private CombinedTicketResponseDto buildResponse(
            Ticket ticket,
            List<TicketTag> tags,
            List<TicketCc> ccs,
            List<TicketAttachment> attachments) {

        Map<Integer, String> priorityMap =
                masterService.getTicketPriorityMap();

        Map<Integer, String> statusMap =
                masterService.getTicketStatusMap();

        MasterContext masterContext =
                new MasterContext(priorityMap, statusMap);

        return new CombinedTicketResponseDto(
                ticketMapper.toTicketDto(ticket, masterContext),
                ticketTagMapper.mapTagsListToDtoList(tags),
                ticketCcMapper.mapTagsListToDtoList(ccs),
                ticketAttachmentMapper.mapTagsListToDtoList(attachments)
        );
    }


    private void sendTicketCreationEmail(Ticket ticket) {

        List<String> toEmails = new ArrayList<>();
        String subject = null;
        String body = null;
        boolean emailSent = false;
        String senderEmail = null;
        try {
            EmailNotificationRequest emailRequest = new EmailNotificationRequest();

            // ---------------- TO ----------------
             toEmails = new ArrayList<>();

            // Assigned user email
            if (ticket.getAssignedTo() != null &&
                    ticket.getAssignedTo().getEmailId() != null) {
                toEmails.add(ticket.getEmailAddress());
            }

            // Customer email
            if (ticket.getEmailAddress() != null) {
                toEmails.add(ticket.getEmailAddress());
            }

            emailRequest.setTo(toEmails);

            // ---------------- CC ----------------
            List<String> ccEmails =
                    ticketCcRepository.findCcEmailsByTicketId(ticket.getTicketId());

            log.info("CC emails from DB: {}", ccEmails);

            emailRequest.setCc(ccEmails);
            // ---------------- SUBJECT ----------------
            subject = "New Ticket Created | Ticket ID : " + ticket.getTicketId();
            emailRequest.setSubject(subject);

            // ---------------- BODY ----------------
             body = String.format(
                    "Dear Customer,\n\n" +
                            "Thank you for contacting the IT Support Team.\n\n" +
                            "We have successfully created your support ticket with the following details:\n\n" +
                            "--------------------------------------------------\n" +
                            "Ticket ID     : %s\n" +
                            "Issue Subject : %s\n" +
                            "Description   : %s\n" +
                            "Priority      : %s\n" +
                            "Department    : %s\n" +
                            "Assigned To   : %s\n" +
                            "--------------------------------------------------\n\n" +
                            "Our team is currently working on your request and will keep you informed of the progress.\n\n" +
                            "Please mention the Ticket ID in all future communications.\n\n" +
                            "Warm Regards,\n" +
                            "Ticket Management System\n" +
                            "IT Support Team",
                    ticket.getTicketId(),
                    ticket.getSubject(),
                    ticket.getDescription(),
                    getPriorityLabel(ticket.getPriority()),
                    ticket.getDepartment().getDepartmentName(),
                    ticket.getAssignedTo().getFirstName() + " " +
                            Objects.toString(ticket.getAssignedTo().getLastName(), "")
            );


            emailRequest.setBody(body);
            Optional<Setting> setting = settingRepository.findByScreen("EMAIL");
             senderEmail = setting.get().getSettingValue();
            emailRequest.setSender(senderEmail);
            emailRequest.setPriority(NotificationPriority.DEFAULT);

            emailUtil.sendEmail(
                    emailRequest,
                    "TICKET_CREATE_" + ticket.getTicketId()
            );
            emailSent = true;

        } catch (Exception e) {
            log.error("Failed to send ticket creation email for ticketId={}",
                    ticket.getTicketId(), e);
        } finally {
            emailUtil.saveEmailLog(
                    toEmails != null ? toEmails : Collections.emptyList(),
                    subject != null ? subject : "N/A",
                    body != null ? body : "N/A",
                    senderEmail,
                    emailSent ? "SUCCESS" : "FAILED",
                    emailSent ? "Email sent successfully" : "Email sending failed"
            );
        }
    }


    private String getPriorityLabel(Integer priority) {
        if (priority == null) {
            return "N/A";
        }
        switch (priority) {
            case 1:
                return "Low";
            case 2:
                return "Medium";
            case 3:
                return "High";
            default:
                return "Unknown";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "DELETE",
            module = "TICKET",
            description = "Ticket id {0} deleted successfully"
    )
    public ApiResponseDTO<Void> deleteTickets(DeleteTicketsRequestDTO requestDTO) {

        try {
            List<Long> ticketIds = requestDTO.getTicketIds();
            if (ticketIds == null || ticketIds.isEmpty()) {
                log.warn("deleteTickets | empty or null ticketIds received");

                return new ApiResponseDTO<>(
                        null, null, null,
                        "Please select at least one ticket to delete.",
                        HttpStatus.BAD_REQUEST,
                        true, null, null
                );
            }

            log.info("deleteTickets | ticketIds count: {}", ticketIds.size());
            log.debug("deleteTickets | ticketIds: {}", ticketIds);

            int updatedCount = ticketRepository.softDeleteByIds(ticketIds);

            log.info("deleteTickets | rows affected: {}", updatedCount);

            if (updatedCount == 0) {
                log.warn("deleteTickets | no tickets updated, possible reasons: not found or already deleted");

                return new ApiResponseDTO<>(
                        null, null, null,
                        "Selected tickets could not be found or may have already been deleted.",
                        HttpStatus.NOT_FOUND,
                        true, null, null
                );
            }

            log.info("deleteTickets | successfully soft deleted {} tickets", updatedCount);
            return new ApiResponseDTO<>(
                    null, null, null,
                    "Selected tickets have been deleted successfully.",
                    HttpStatus.OK,
                    false, null, null
            );

        } catch (Exception e) {
            log.error("deleteTickets unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    public ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>> searchTickets(
            TicketSearchRequestDto dto) {

        log.info("Ticket search started | query='{}', page={}, size={}, sortBy={}, sortDir={}",
                dto.getQuery(),
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir()
        );

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Ticket> pageResult =
                    ticketRepository.searchTickets(dto.getQuery(), pageable);

            if (pageResult.isEmpty()) {
                log.warn("No tickets found for query='{}'", dto.getQuery());

                return new ApiResponseDTO<>(
                        null,
                        "No tickets matched your search criteria",
                        HttpStatus.NOT_FOUND,
                        false
                );
            }

            List<TicketCombinedResponseDto> content =
                    pageResult.getContent()
                            .stream()
                            .map(this::mapToCombinedDto)
                            .collect(Collectors.toList());

            log.info("Ticket search success | query='{}', results={}, page={}/{}",
                    dto.getQuery(),
                    content.size(),
                    pageResult.getNumber() + 1,
                    pageResult.getTotalPages()
            );

            PagedResponse<TicketCombinedResponseDto> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Tickets fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {

            log.error("Error while searching tickets | query='{}'", dto.getQuery(), e);

            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while searching tickets. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private TicketCombinedResponseDto mapToCombinedDto(Ticket ticket) {

        TicketCombinedResponseDto dto = new TicketCombinedResponseDto();
        if (ticket.getCreatedBy() != null) {
            dto.setCreatedBy(ticket.getCreatedBy().getUserName());
        }

        if (ticket.getModifiedBy() != null) {
            dto.setModifiedBy(ticket.getModifiedBy().getUserName());
        }

        dto.setCreatedTime(ticket.getCreatedTime());
        dto.setModifiedTime(ticket.getModifiedTime());

        dto.setTicketId(ticket.getTicketId());
        dto.setSubject(ticket.getSubject());
        dto.setComplaintName(ticket.getComplaintName());
        dto.setComplaintMobileNo(ticket.getComplaintMobileNo());

        // ---------------- Department ----------------
        if (ticket.getDepartment() != null) {
            dto.setDepartmentId(ticket.getDepartment().getDepartmentId());
            dto.setDepartmentName(ticket.getDepartment().getDepartmentName());
        }

        // ---------------- Service ----------------
        if (ticket.getService() != null) {
            dto.setServiceId(ticket.getService().getServiceId());
            dto.setServiceName(ticket.getService().getServiceName());
        }

        // ---------------- Status (Master Table) ----------------
        if (ticket.getStatus() != null) {
            Masters status = mastersRepository.findByColumnCodeAndColumnValue(2, ticket.getStatus());
            dto.setStatus(status.getValueDesc());
        }

        // ---------------- Priority ----------------
        dto.setPriority(getPriorityLabel(ticket.getPriority()));

        // ---------------- Last Reply ----------------
        List<LocalDateTime> replies =
                ticketResponseRepository.findLastReplyTime(ticket.getTicketId());
        if (replies != null && !replies.isEmpty()) {
            dto.setLastReply(replies.get(0));
        }


        // ---------------- Tags ----------------
        if (ticket.getTicketTags() != null && !ticket.getTicketTags().isEmpty()) {

            dto.setTagIds(
                    ticket.getTicketTags()
                            .stream()
                            .map(t -> t.getTags().getTagId())
                            .collect(Collectors.toList())
            );

            dto.setTagName(
                    ticket.getTicketTags()
                            .stream()
                            .map(t -> t.getTags().getTagName())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "UPDATE",
            module = "TICKET",
            description = "Ticket {0} updated successfully"
    )
    public ApiResponseDTO<CombinedTicketResponseDto> updateTickets(
            Long ticketId,
            String ticketRequestDto,
            List<MultipartFile> multipartFiles) {

        log.info("<<START>> updateTickets service ticketId={}", ticketId);

        try {
            NewTicketsRequestDTO requestDto =
                    objectMapper.readValue(ticketRequestDto, NewTicketsRequestDTO.class);

            TicketsResponseDTO ticketDto = requestDto.getTicketsResponseDTO();

            Ticket existingTicket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            Users assignedUser = validateAndGetAssignedUser(ticketDto);

            updateTicketCoreFields(existingTicket, ticketDto, assignedUser);

            Ticket updatedTicket = ticketRepository.save(existingTicket);

            List<TicketTag> tags =
                    updateTicketTags(
                            requestDto.getTicketTagResponseDTOS(),
                            updatedTicket);

            List<TicketCc> ccs =
                    updateTicketCcs(
                            requestDto.getTicketCcResponseDTOS(),
                            updatedTicket);

            List<TicketAttachment> attachments =
                    saveTicketAttachments(
                            multipartFiles,
                            updatedTicket,
                            assignedUser);

            Ticket fullTicket =
                    ticketRepository.findByIdWithAllDetails(updatedTicket.getTicketId())
                            .orElseThrow(() -> new RuntimeException("Ticket not found after update"));

            CombinedTicketResponseDto responseDto =
                    buildResponse(fullTicket, tags, ccs, attachments);

            log.info("<<END>> updateTickets service SUCCESS ticketId={}", ticketId);

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Ticket updated successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in updateTickets service ticketId={}", ticketId, e);
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


    private void updateTicketCoreFields(
            Ticket ticket,
            TicketsResponseDTO dto,
            Users assignedUser) {

        if (dto.getSubject() != null)
            ticket.setSubject(dto.getSubject().trim());

        if (dto.getDescription() != null)
            ticket.setDescription(dto.getDescription());

        if (dto.getComplaintName() != null)
            ticket.setComplaintName(dto.getComplaintName());

        if (dto.getComplaintMobileNo() != null)
            ticket.setComplaintMobileNo(dto.getComplaintMobileNo());

        if (dto.getEmailAddress() != null)
            ticket.setEmailAddress(dto.getEmailAddress());

        if (dto.getPriority() != null)
            ticket.setPriority(dto.getPriority());

        if (dto.getStatus() != null)
            ticket.setStatus(dto.getStatus());

        ticket.setAssignedTo(assignedUser);

        if (dto.getLocation() != null)
            ticket.setLocation(getLocation(dto));

        if (dto.getContact() != null)
            ticket.setContact(getContact(dto));

        if (dto.getDepartment() != null)
            ticket.setDepartment(getDepartment(dto));

        if (dto.getService() != null)
            ticket.setService(getService(dto));

        if (dto.getClientProducts() != null)
            ticket.setClientProducts(getClientProduct(dto));

        if (dto.getBranch() != null)
            ticket.setBranch(getBranch(dto));
    }

    @Transactional
    private List<TicketTag> updateTicketTags(
            List<TicketTagResponseDTO> incomingDtos,
            Ticket ticket) {

        if (incomingDtos == null)
            return ticketTagRepository.findByTicket(ticket);

        List<TicketTag> existingTags =
                ticketTagRepository.findByTicket(ticket);

        Set<Long> existingTagIds = existingTags.stream()
                .map(t -> t.getTags().getTagId())
                .collect(Collectors.toSet());

        Set<Long> incomingTagIds = incomingDtos.stream()
                .map(dto -> dto.getTags().getTagId())
                .collect(Collectors.toSet());

        /* -------- DELETE -------- */
        List<TicketTag> toDelete = existingTags.stream()
                .filter(t -> !incomingTagIds.contains(t.getTags().getTagId()))
                .collect(Collectors.toList());

        ticketTagRepository.deleteAll(toDelete);

        /* -------- ADD -------- */
        List<TicketTag> toAdd = incomingTagIds.stream()
                .filter(id -> !existingTagIds.contains(id))
                .map(id -> {
                    Tags tag = tagsRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Tag not found " + id));
                    TicketTag tagEntity = new TicketTag();
                    tagEntity.setTicket(ticket);
                    tagEntity.setTags(tag);
                    return tagEntity;
                })
                .collect(Collectors.toList());

        ticketTagRepository.saveAll(toAdd);

        return ticketTagRepository.findByTicket(ticket);
    }

    @Transactional
    private List<TicketCc> updateTicketCcs(
            List<TicketCcResponseDTO> incomingDtos,
            Ticket ticket) {

        if (incomingDtos == null)
            return ticketCcRepository.findByTicket(ticket);

        List<TicketCc> existingCcs =
                ticketCcRepository.findByTicket(ticket);

        Set<String> existingEmails = existingCcs.stream()
                .map(TicketCc::getCc)
                .collect(Collectors.toSet());

        Set<String> incomingEmails = incomingDtos.stream()
                .map(TicketCcResponseDTO::getCc)
                .filter(email -> email != null && !email.isBlank())
                .collect(Collectors.toSet());

        /* -------- DELETE -------- */
        List<TicketCc> toDelete = existingCcs.stream()
                .filter(cc -> !incomingEmails.contains(cc.getCc()))
                .collect(Collectors.toList());

        ticketCcRepository.deleteAll(toDelete);

        /* -------- ADD -------- */
        List<TicketCc> toAdd = incomingEmails.stream()
                .filter(email -> !existingEmails.contains(email))
                .map(email -> {
                    TicketCc cc = new TicketCc();
                    cc.setTicket(ticket);
                    cc.setCc(email);
                    return cc;
                })
                .collect(Collectors.toList());

        ticketCcRepository.saveAll(toAdd);

        return ticketCcRepository.findByTicket(ticket);
    }



    @Override
    public ApiResponseDTO<SummaryKpiResponseDTO> getTikctSummary() {
        log.info("Fetching Ticket Summary KPI...");

        try {
            List<Object[]> result = ticketRepository.countTicketsByStatus();

            List<SummaryKpiResponseDTO> summaryList =
                    result.stream()
                            .map(obj -> new SummaryKpiResponseDTO(
                                    String.valueOf(obj[0]),   // status
                                    obj[1]                    // count
                            ))
                            .collect(Collectors.toList());

            log.info("Ticket summary fetched successfully. Total statuses: {}", summaryList.size());

            return new ApiResponseDTO<>(
                    null,
                    summaryList,
                    null,
                    "Ticket summary fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Error fetching ticket summary KPI: {}", e.getMessage(), e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Failed to fetch ticket summary",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportTickets(
            ExportSearchRequestDTO request,
            HttpServletResponse response
    ) {

        String format = request.getFormat();
        ExportFilterDTO filters = request.getFilters();
        String search = filters != null ? filters.getSearch() : null;

        log.info("Ticket export requested | format={} | search={}", format, search);

        if (format == null ||
                !List.of("excel", "csv", "pdf")
                        .contains(format.toLowerCase())) {

            log.warn("Ticket export rejected | unsupported format={}", format);
            throw new IllegalArgumentException("Unsupported export format");
        }

        try {

            log.info("Fetching tickets for export | search={}", search);

            List<Ticket> tickets =
                    ticketRepository
                            .searchTickets(search, Pageable.unpaged())
                            .getContent();

            log.info("Tickets fetched successfully | count={}", tickets.size());

            Map<Integer, String> priorityMap =
                    masterService.getTicketPriorityMap();

            Map<Integer, String> statusMap =
                    masterService.getTicketStatusMap();

            MasterContext masterContext =
                    new MasterContext(priorityMap, statusMap);

            List<TicketsResponseDTO> dtos =
                    ticketMapper.toTicketDtoList(tickets, masterContext);
            TicketExcelExportHelper.export(dtos, format, response);

            log.info(
                    "Ticket export completed successfully | format={} | records={}",
                    format, dtos.size()
            );

        } catch (Exception e) {
            log.error("Ticket export failed due to unexpected error", e);
            throw new RuntimeException("Failed to export tickets", e);
        }
    }

}




