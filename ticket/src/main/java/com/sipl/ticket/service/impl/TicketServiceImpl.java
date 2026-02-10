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
import com.sipl.ticket.core.enums.WorkFlowStatusEnum;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.TicketExcelExportHelper;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.core.util.EmailUtil;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.core.util.TicketFileUploadUtil;
import com.sipl.ticket.core.util.UserManager;
import com.sipl.ticket.master.service.MasterService;
import com.sipl.ticket.service.EmailWorkflowService;
import com.sipl.ticket.service.TicketService;
import com.sipl.ticket.service.WorkFlowInstanceService;
import com.sipl.ticket.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final TicketNoteMapper ticketNoteMapper;
    private final TicketNoteRepository ticketNoteRepository;
    private final ShiftRepository shiftRepository;
    private final WorkFlowDefinitionRepository workFlowDefinitionRepository;
    private final WorkflowStepsRepository workflowStepsRepository;
    private final TaskRepository taskRepository;

    private final WorkflowInstanceMapper workflowInstanceMapper;
    private final WorkFlowInstanceService workFlowInstanceService;
    private final WorkflowNotificationService workflowNotificationService;
    private final EmailWorkflowService emailWorkflowService;
    private final @Qualifier("helperUserManager") UserManager userManager;
    private final HttpServletRequest request;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "CREATE",
            module = "TICKET",
            description = "Ticket {0} created successfully"
    )
    public ApiResponseDTO<CombinedTicketResponseDto> addTickets(
            String ticketRequestDto,
            List<MultipartFile> files) {
        try {
            NewTicketsRequestDTO dto =
                    objectMapper.readValue(ticketRequestDto, NewTicketsRequestDTO.class);
            Ticket ticket = saveTicket(dto);
            List<TicketTag> tags = saveTicketTags(dto.getTagIds(), ticket);
            List<TicketCc> ccs = saveTicketCcs(dto.getCcEmails(), ticket);
            List<TicketAttachment> attachments =
                    saveTicketAttachments(files, ticket, ticket.getAssignedTo());
            CombinedTicketResponseDto responseDto =
                    buildResponse(ticket, tags, ccs, attachments);
            sendTicketCreationEmail(ticket);
            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Ticket created successfully",
                    HttpStatus.CREATED,
                    false,
                    null,
                    null
            );
        } catch (Exception e) {
            log.error("Exception in addTickets service", e);
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

    private Ticket saveTicket(NewTicketsRequestDTO dto) {
        validateTicketRequest(dto);
        Users assignedUser =
                userRepository.findById(dto.getAssignedTo().getId())
                        .orElseThrow(() -> new RuntimeException("Assigned user not found"));
        Branches branch = branchesRepository.findById(dto.getBranch().getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        Department department = departmentRepository.findById(dto.getDepartment().getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Locations location = locationRepository.findById(dto.getLocation().getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        Ticket ticket = new Ticket();
        mapBasicTicketFields(dto, ticket);
        mapOptionalTicketFields(dto, ticket);
        ticket.setAssignedTo(assignedUser);
        ticket.setBranch(branch);
        ticket.setDepartment(department);
        ticket.setLocation(location);
        List<Shift> shifts = shiftRepository.findByBranchId(branch.getBranchId());
        Shift shiftForTicket = getShiftForTicket(shifts);
        ticket.setShift(shiftForTicket);
        return ticketRepository.save(ticket);
    }
    private Shift getShiftForTicket(List<Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) return null;

        LocalTime now = LocalTime.now();

        for (Shift s : shifts) {
            if (Boolean.TRUE.equals(s.getIsActive())) {
                LocalTime start = s.getStartTime();
                LocalTime end = s.getEndTime();

                boolean crossesMidnight = end.isBefore(start);

                boolean inShift = crossesMidnight
                        ? (now.isAfter(start) || now.isBefore(end))
                        : (now.isAfter(start) && now.isBefore(end));

                if (inShift) {
                    Shift shift = new Shift();
                    shift.setShiftId(s.getShiftId());
                    return shift;
                }
            }
        }
        return null;
    }


    private void mapOptionalTicketFields(NewTicketsRequestDTO dto, Ticket ticket) {
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(dto.getStatus());
        if (dto.getService() != null)
            ticket.setService(
                    serviceRepository.findById(dto.getService().getServiceId())
                            .orElseThrow(() -> new RuntimeException("Service not found"))
            );
        if (dto.getClientProducts() != null)
            ticket.setClientProducts(
                    clientProductsRepository.findById(
                                    dto.getClientProducts().getClientProductId())
                            .orElseThrow(() -> new RuntimeException("Client product not found"))
            );
        if (dto.getContact() != null)
            ticket.setContact(
                    contactsRepository.findById(dto.getContact().getContactId())
                            .orElseThrow(() -> new RuntimeException("Contact not found"))
            );
    }


    private void validateTicketRequest(NewTicketsRequestDTO dto) {
        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty()) {
            throw new RuntimeException("Ticket subject is required");
        }
        if (dto.getBranch() == null || dto.getBranch().getBranchId() == null) {
            throw new RuntimeException("Branch is required");
        }
        if (dto.getDepartment() == null) {
            throw new RuntimeException("Department is required");
        }
        if (dto.getAssignedTo() == null) {
            throw new RuntimeException("Assigned user is required");
        }
    }

    private void mapBasicTicketFields(NewTicketsRequestDTO dto, Ticket ticket) {
        ticket.setSubject(dto.getSubject().trim());
        ticket.setDescription(dto.getDescription());
        ticket.setComplaintName(dto.getComplaintName());
        ticket.setComplaintMobileNo(dto.getComplaintMobileNo());
        ticket.setEmailAddress(dto.getEmailAddress());
    }


    @Transactional
    private List<TicketTag> saveTicketTags(
            List<Long> tagIds,
            Ticket ticket) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<TicketTag> tags = tagIds.stream()
                .distinct()
                .map(tagId -> {
                    Tags tag = tagsRepository.findById(tagId)
                            .orElseThrow(() ->
                                    new RuntimeException("Tag not found with id " + tagId));

                    TicketTag ticketTag = new TicketTag();
                    ticketTag.setTicket(ticket);
                    ticketTag.setTags(tag);
                    return ticketTag;
                })
                .collect(Collectors.toList());
        return ticketTagRepository.saveAll(tags);
    }

    @Transactional
    private List<TicketCc> saveTicketCcs(
            List<String> ccEmails,
            Ticket ticket) {
        if (ccEmails == null || ccEmails.isEmpty()) {
            return Collections.emptyList();
        }
        List<TicketCc> ccs = ccEmails.stream()
                .filter(email -> email != null && !email.isBlank())
                .distinct()
                .map(email -> {
                    TicketCc cc = new TicketCc();
                    cc.setTicket(ticket);
                    cc.setCc(email.trim());
                    return cc;
                })
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

    private Users validateAndGetAssignedUser(NewTicketsRequestDTO  dto) {
            if (dto.getAssignedTo() == null || dto.getAssignedTo().getId() == null) {
                throw new RuntimeException("AssignedTo user is required");
            }
            return userRepository.findById(dto.getAssignedTo().getId())
                    .orElseThrow(() ->
                            new RuntimeException("AssignedTo user not found"));
        }

        private Locations getLocation(NewTicketsRequestDTO dto) {
            return locationRepository.findById(dto.getLocation().getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
        }

        private Contact getContact(NewTicketsRequestDTO dto) {
            return contactsRepository.findById(dto.getContact().getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found"));
        }

        private Department getDepartment(NewTicketsRequestDTO dto) {
            return departmentRepository.findById(dto.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }

        private ServiceEntity getService(NewTicketsRequestDTO dto) {
            return serviceRepository.findById(dto.getService().getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
        }

        private ClientProducts getClientProduct(NewTicketsRequestDTO dto) {
            return clientProductsRepository.findById(dto.getClientProducts().getClientProductId())
                    .orElseThrow(() -> new RuntimeException("Client Product not found"));
        }

        private Branches getBranch(NewTicketsRequestDTO dto) {
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

        log.info("Ticket search started | query='{}', page={}, size={}, sortBy={}, sortDir={}, branchId={}",
                dto.getQuery(),
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir(),
                dto.getBranchId()
        );

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Ticket> pageResult =
                    ticketRepository.searchTickets(dto.getQuery(),dto.getBranchId(),dto.getTicketStatus(), pageable);

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
        if (ticket.getDepartment() != null) {
            dto.setDepartmentId(ticket.getDepartment().getDepartmentId());
            dto.setDepartmentName(ticket.getDepartment().getDepartmentName());
        }
        if (ticket.getService() != null) {
            dto.setServiceId(ticket.getService().getServiceId());
            dto.setServiceName(ticket.getService().getServiceName());
        }
        if (ticket.getStatus() != null) {
            Masters status = mastersRepository.findByColumnCodeAndColumnValue(2, ticket.getStatus());
            dto.setStatus(status.getValueDesc());
        }
        dto.setPriority(getPriorityLabel(ticket.getPriority()));
        dto.setLastReply(ticket.getResponseDateTime());
        List<LocalDateTime> replies =
                ticketResponseRepository.findLastReplyTime(ticket.getTicketId());
        if (replies != null && !replies.isEmpty()) {
            dto.setLastReply(replies.get(0));
        }
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
            String ticketRequestDto,
            List<MultipartFile> multipartFiles) {
        log.info("<<START>> updateTickets service");
        try {
            NewTicketsRequestDTO dto =
                    objectMapper.readValue(ticketRequestDto, NewTicketsRequestDTO.class);
            if (dto.getTicketId() == null) {
                throw new RuntimeException("Ticket ID is required for update");
            }
            Ticket ticket = ticketRepository.findById(dto.getTicketId())
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));
            Users assignedUser = validateAndGetAssignedUser(dto);
            updateTicketCoreFields(ticket, dto, assignedUser);
            Ticket updatedTicket = ticketRepository.save(ticket);
            List<TicketTag> tags =
                    updateTicketTags(dto.getTagIds(), updatedTicket);
            List<TicketCc> ccs =
                    updateTicketCcs(dto.getCcEmails(), updatedTicket);
            List<TicketAttachment> attachments =
                    saveTicketAttachments(multipartFiles, updatedTicket, assignedUser);
            Ticket fullTicket =
                    ticketRepository.findByIdWithAllDetails(updatedTicket.getTicketId())
                            .orElseThrow(() ->
                                    new RuntimeException("Ticket not found after update"));
            CombinedTicketResponseDto responseDto =
                    buildResponse(fullTicket, tags, ccs, attachments);
            log.info("<<END>> updateTickets service SUCCESS ticketId={}", updatedTicket.getTicketId());
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
            log.error("Exception in updateTickets service", e);
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
            NewTicketsRequestDTO dto,
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
            List<Long> incomingTagIds,
            Ticket ticket) {
        List<TicketTag> existingTags =
                ticketTagRepository.findByTicket(ticket);
        if (incomingTagIds == null)
            return existingTags;
        Set<Long> existingIds = existingTags.stream()
                .map(t -> t.getTags().getTagId())
                .collect(Collectors.toSet());
        Set<Long> incomingIds = new HashSet<>(incomingTagIds);
        ticketTagRepository.deleteAll(
                existingTags.stream()
                        .filter(t -> !incomingIds.contains(t.getTags().getTagId()))
                        .collect(Collectors.toList())
        );
        List<TicketTag> toAdd = incomingIds.stream()
                .filter(id -> !existingIds.contains(id))
                .map(id -> {
                    Tags tag = tagsRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Tag not found " + id));
                    TicketTag tt = new TicketTag();
                    tt.setTicket(ticket);
                    tt.setTags(tag);
                    return tt;
                })
                .collect(Collectors.toList());

        ticketTagRepository.saveAll(toAdd);

        return ticketTagRepository.findByTicket(ticket);
    }


    @Transactional
    private List<TicketCc> updateTicketCcs(
            List<String> incomingEmails,
            Ticket ticket) {
        List<TicketCc> existingCcs =
                ticketCcRepository.findByTicket(ticket);
        if (incomingEmails == null)
            return existingCcs;
        Set<String> existingSet = existingCcs.stream()
                .map(TicketCc::getCc)
                .collect(Collectors.toSet());
        Set<String> incomingSet = incomingEmails.stream()
                .filter(e -> e != null && !e.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());
        ticketCcRepository.deleteAll(
                existingCcs.stream()
                        .filter(cc -> !incomingSet.contains(cc.getCc()))
                        .collect(Collectors.toList())
        );
        List<TicketCc> toAdd = incomingSet.stream()
                .filter(email -> !existingSet.contains(email))
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
            List<Object[]> ticketCounts =
                    ticketRepository.countTicketsByStatus();
            Map<String, Long> countMap = new HashMap<>();
            for (Object[] row : ticketCounts) {
                String statusName = row[0].toString();
                Long count = ((Number) row[1]).longValue();
                countMap.put(statusName, count);
            }
            List<Masters> statuses =
                    mastersRepository.findAllActiveStatuses(2);
            List<SummaryKpiResponseDTO> summaryList =
                    statuses.stream()
                            .map(master -> new SummaryKpiResponseDTO(
                                    master.getValueDesc(),
                                    countMap.getOrDefault(
                                            master.getValueDesc(), 0L
                                    )
                            ))
                            .collect(Collectors.toList());
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
            log.error("Error fetching ticket summary KPI", e);

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
                            .searchTickets(search, null, null,Pageable.unpaged())
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
    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<Long> getAllTicketIds() {

        try {

            List<Long> ticketIds =
                    ticketRepository.findAllActiveTicketIds();

            if (ticketIds == null || ticketIds.isEmpty()) {

                log.warn("getAllTicketIds | No active tickets found");

                return new ApiResponseDTO<Long>(
                        null,
                        Collections.emptyList(),
                        null,
                        "No active tickets are available at the moment.",
                        HttpStatus.NOT_FOUND,
                        false,
                        null,
                        null
                );
            }

            log.info(
                    "getAllTicketIds | Successfully fetched {} active ticket IDs",
                    ticketIds.size()
            );

            return new ApiResponseDTO<Long>(
                    null,
                    ticketIds,
                    null,
                    "Active ticket IDs fetched successfully.",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {

            log.error(
                    "getAllTicketIds | Unexpected error while fetching active ticket IDs",
                    e
            );

            return new ApiResponseDTO<Long>(
                    null,
                    null,
                    null,
                    "Something went wrong while fetching ticket IDs. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @Override
    public ApiResponseDTO<CombinedTicketNoteResponseDto> getByTicketId(Long ticketId) {
        try {
            log.info("Fetching ticket details for ticketId={}", ticketId);
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Ticket not found with id: " + ticketId));
            Map<Integer, String> priorityMap = masterService.getTicketPriorityMap();
            Map<Integer, String> statusMap = masterService.getTicketStatusMap();
            MasterContext masterContext = new MasterContext(priorityMap, statusMap);
            TicketsResponseDTO ticketDto = ticketMapper.toTicketDto(ticket, masterContext);
            List<TicketTagResponseDTO> tagDtos =
                    ticketTagMapper.toDtoList(ticketTagRepository.findByTicketId(ticketId));
            List<TicketCcResponseDTO> ccDtos =
                    ticketCcMapper.toDtoList(ticketCcRepository.findByTicketId(ticketId));
            List<TicketAttachmentResponseDTO> attachmentDtos =  ticketAttachmentMapper.toDtoList(ticketAttachmentRepository.findByTicketId(ticketId));
            List<TicketNoteResponseDTO> ticketNotes =  ticketNoteMapper.mapTicketNoteListToDtoList(ticketNoteRepository.findByTicketId(ticketId));
            CombinedTicketNoteResponseDto response = new CombinedTicketNoteResponseDto(
                    ticketDto,
                    tagDtos,
                    ccDtos,
                    attachmentDtos,
                    ticketNotes
            );
            return new ApiResponseDTO<>(
                    response,
                    "Ticket details fetched successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (EntityNotFoundException ex) {
            log.error("Ticket not found for ticketId={}", ticketId, ex);
            return new ApiResponseDTO<>(null, ex.getMessage(), HttpStatus.NOT_FOUND, true);
        } catch (Exception ex) {
            log.error("Unexpected error while fetching ticketId={}", ticketId, ex);
            return new ApiResponseDTO<>(null, "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    public ApiResponseDTO<TicketCombinedResponseDto> updateTicketStatus(
            TicketStatusRequestDTO dto) {

        log.info("Update ticket status request received. ticketId={}, status={}",
                dto != null ? dto.getTicketId() : null,
                dto != null ? dto.getStatus() : null
        );

        try {
            log.debug("Fetching ticket. ticketId={}", dto.getTicketId());

            Ticket ticket = ticketRepository.findById(dto.getTicketId())
                    .orElseThrow(() -> {
                        log.warn("Ticket not found. ticketId={}", dto.getTicketId());
                        return new ResourceNotFoundException("Ticket not found");
                    });

            Integer oldStatus = ticket.getStatus();

            // If ticket is Approval Pending (7), do not allow status change
            if (oldStatus != null && oldStatus == 7) {
                log.warn(
                        "Ticket is in Approval Pending state. Status change not allowed. ticketId={}",
                        ticket.getTicketId()
                );

                throw new IllegalStateException(
                        "Ticket is approval pending. Status cannot be changed."
                );
            }

            log.debug("Loading ticket master maps");
            Map<Integer, String> priorityMap = masterService.getTicketPriorityMap();
            Map<Integer, String> statusMap = masterService.getTicketStatusMap();

            if (!statusMap.containsKey(dto.getStatus())) {
                log.warn("Invalid status code received. status={}", dto.getStatus());
                throw new IllegalArgumentException(
                        "Invalid status code: " + dto.getStatus()
                );
            }

            // If trying to close the ticket (5)
            if (dto.getStatus() != null && dto.getStatus() == 5) {

                boolean hasIncompleteTasks =
                        taskRepository.existsByTicketIdAndStatusNot(
                                ticket.getTicketId(),
                                5
                        );

                if (hasIncompleteTasks) {
                    log.warn(
                            "Cannot close ticket. Pending tasks exist. ticketId={}",
                            ticket.getTicketId()
                    );

                    throw new IllegalStateException(
                            "All tasks must be completed before closing the ticket."
                    );
                }

                // Resolution datetime set when ticket is closed
                ticket.setResolutionDateTime(LocalDateTime.now());
            }

            /* ================= UPDATE ================= */
            log.info("Updating ticket status. ticketId={}, oldStatus={}, newStatus={}",
                    dto.getTicketId(), oldStatus, dto.getStatus());

            ticket.setStatus(dto.getStatus());
            Ticket updatedTicket = ticketRepository.save(ticket);

            MasterContext masterContext =
                    new MasterContext(priorityMap, statusMap);

            TicketCombinedResponseDto responseDto =
                    ticketMapper.toCombinedResponseDto(updatedTicket, masterContext);

            log.info("Ticket status updated successfully. ticketId={}",
                    dto.getTicketId());

            return new ApiResponseDTO<>(
                    responseDto,
                    "Ticket status updated successfully",
                    HttpStatus.OK,
                    false
            );

        }catch (IllegalStateException ex) {

                log.warn(
                        "Business validation failed while updating ticket status. ticketId={}, message={}",
                        dto != null ? dto.getTicketId() : null,
                        ex.getMessage()
                );

                return new ApiResponseDTO<>(
                        null,
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        true
                );
            } catch (Exception ex) {
            log.error("Error occurred while updating ticket status. ticketId={}",
                    dto != null ? dto.getTicketId() : null, ex);

            throw new RuntimeException(
                    "Failed to update ticket status",
                    ex
            );
        }
    }


    @Override
    public ApiResponseDTO<String> requestTicketApproval(ApprovalRequestDTO dto) {
        log.info("Requesting approval for ticketId={}", dto.getTicketId());

        try {
            Optional<Ticket> fetchTicketData = ticketRepository.findById(dto.getTicketId());
            if (!fetchTicketData.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Ticket data not found",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Ticket ticket = fetchTicketData.get();
            if (ticket.getStatus() != null && ticket.getStatus().equals(5)) { // Closed
                return new ApiResponseDTO<>(
                        null,
                        "Ticket already closed, approval cannot be requested",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            if(ticket.getStatus().equals(7))
            {
                return new ApiResponseDTO<>(
                        null,
                        "Ticket already in approval process",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            // Update status to "Approval Pending"
            ticket.setStatus(7);
            ticket.setIsApproverRequired(true);
            ticket.setIsApproved(false);
            Optional<Users> fetchedUsers=userRepository.findById(dto.getAssignedBy());
            if(!fetchedUsers.isPresent())
            {
                return new ApiResponseDTO<>(
                        null,
                        "Assigned user data not found",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            Users user=fetchedUsers.get();
            createWorkflowInstance(ticket,user,dto.getReason());
            ticketRepository.save(ticket);

            return new ApiResponseDTO<>(
                    null,
                    "Ticket approval requested successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception ex) {
            log.error("Error while requesting ticket approval for ticketId={}, msg={}",
                    dto.getTicketId(), ex.getMessage(), ex);

            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while requesting approval",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<TicketCustomResponseDto> getAllTicketCustomDetails() {

        try {

            List<Ticket> tickets = ticketRepository.findByStatusNot(5);

            if (tickets.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No tickets found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<TicketCustomResponseDto> response = tickets.stream()
                    .map(t -> new TicketCustomResponseDto(
                            t.getTicketId(),
                            t.getSubject(),
                            "#" + t.getTicketId() + "-" + t.getSubject()
                    ))
                    .collect(Collectors.toList());


            return new ApiResponseDTO<TicketCustomResponseDto>(
                    null,
                    response,
                    null,
                    "Tickets fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            return new ApiResponseDTO<>(
                    null,
                    "Failed to fetch tickets",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private WorkflowInstanceDTO createWorkflowInstance(Ticket ticket,Users assignedTo,String reason) {
        log.info("Creating workflow instance for ticket: {}", ticket.getTicketId());
        final String entityType = "Ticket";
        WorkFlowDefinition workflowDefinition = workFlowDefinitionRepository.findByEntityType(entityType)
                .orElseThrow(() -> new RuntimeException("Workflow Definition for 'Transaction' not found"));

        log.info("Found workflowDefinitionId: {}", workflowDefinition.getWorkFlowDefinitionId());

        WorkflowSteps firstStep = workflowStepsRepository
                .findFirstStepByDefinitionId(workflowDefinition.getWorkFlowDefinitionId())
                .orElseThrow(() -> new RuntimeException(
                        "Step with stepOrder not found for workflowDefinitionId=" + workflowDefinition.getWorkFlowDefinitionId()));

        log.info("Found first workflow stepId: {}", firstStep.getWorkFlowStepsId());

        WorkFlowDefinitionDTO defDto = new WorkFlowDefinitionDTO();
        defDto.setWorkFlowDefinitionId(workflowDefinition.getWorkFlowDefinitionId());

        WorkflowStepsDTO stepDto = new WorkflowStepsDTO();
        stepDto.setWorkFlowStepsId(firstStep.getWorkFlowStepsId());
        WorkflowInstanceDTO instanceDto = new WorkflowInstanceDTO();
        instanceDto.setWorkflow(defDto);
        instanceDto.setCurrentStep(stepDto);
        instanceDto.setEntityId(ticket.getTicketId());
        instanceDto.setEntityType(entityType);
        instanceDto.setWorkFlowStatus(WorkFlowStatusEnum.CREATED.getCode());
        instanceDto.setStartedAt(LocalDateTime.now());
        instanceDto.setAssignedUser(assignedTo);
        instanceDto.setReason(reason);
        ApiResponseDTO<WorkflowInstanceDTO> instanceResponse =
                workFlowInstanceService.addWorkflowInstance(instanceDto);
        WorkflowInstance workflowInstance = workflowInstanceMapper.toEntity(instanceResponse.getData());
        log.info("workflowInstance: {}", workflowInstance);
        String stepStatus="CREATED";
        String mailStatus=null;
        Users user=userManager.getUser(request);
        if (stepStatus != null) {
            mailStatus = emailWorkflowService.sendWorkflowEmail(workflowInstance,user, firstStep, stepStatus);
            if (mailStatus.equals("Failed")) {
                log.error("Failed to send mail");
                mailStatus = "Failed";
            }
        }
        log.info("Workflow instance created successfully for ticketId: {}", ticket.getTicketId());
        return instanceResponse.getData();
    }
}




