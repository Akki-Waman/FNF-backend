package com.sipl.ticket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.callback.DocumentServiceCallback;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.NewTicketsRequestDTO;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.helper.DmsUploadResult;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.core.util.TicketFileUploadUtil;
import com.sipl.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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
    private final UserMapper userMapper;
    private final UsersRepository userRepository;
    private final LocationRepository locationRepository;
    private final ContactRepository contactsRepository;
    private final DepartmentRepository departmentRepository;
    private final ClientProductsRepository clientProductsRepository;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchesRepository;
    private final DmsDocumentRepository dmsDocumentRepository;
    private final DmsDocumentMetadataRepository dmsDocumentMetadataRepository;
    private final DmsErrorLogRepository dmsErrorLogRepository;
    private final DocumentClientService documentClientService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDTO<CombinedTicketResponseDto> addTickets(
            Long ticketId,
            String ticketRequestDto,
            List<MultipartFile> multipartFiles) {

        log.info("<<START>> addTickets service");

        try {
            log.debug("Parsing ticketRequestDto JSON");
            NewTicketsRequestDTO requestDto =
                    objectMapper.readValue(ticketRequestDto, NewTicketsRequestDTO.class);

            TicketsResponseDTO ticketDto = requestDto.getTicketsResponseDTO();

            log.info("Validating assigned user");
            Users assignedUser = validateAndGetAssignedUser(ticketDto);

            log.info("Saving ticket");
            Ticket savedTicket = saveTicket(ticketDto, assignedUser);
            log.info("Ticket saved with ID: {}", savedTicket.getTicketId());

            log.info("Saving ticket tags");
            List<TicketTag> ticketTags =
                    saveTicketTags(requestDto.getTicketTagResponseDTOS(), savedTicket);

            log.info("Saving ticket CCs");
            List<TicketCc> ticketCcs =
                    saveTicketCcs(requestDto.getTicketCcResponseDTOS(), savedTicket);

            log.info("Saving ticket attachments");
            List<TicketAttachment> attachments =
                    saveTicketAttachments(multipartFiles, savedTicket, assignedUser, ticketRequestDto);

            log.info("Fetching full ticket with all relations");
            Ticket fullTicket =
                    ticketRepository.findByIdWithAllDetails(savedTicket.getTicketId())
                            .orElseThrow(() ->
                                    new RuntimeException("Ticket not found after save"));

            CombinedTicketResponseDto responseDto =
                    buildResponse(fullTicket, ticketTags, ticketCcs, attachments);

            log.info("<<END>> addTickets service SUCCESS");

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
            log.error("❌ Exception in addTickets service", e);

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

    @Transactional(rollbackFor = Exception.class)
    private List<TicketAttachment> saveTicketAttachments(
            List<MultipartFile> files,
            Ticket ticket,
            Users uploadedBy,
            String ticketRequestDto
    ) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        List<TicketAttachment> attachments = new ArrayList<>();
        String moduleName = "tickets";
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            String originalFileName = file.getOriginalFilename();
            log.info("Processing attachment [{}] for ticket {} RequestId={}",
                    originalFileName, ticket.getTicketId(), ticketRequestDto);
            try {
                byte[] fileBytes = file.getBytes();
                String savedPath = fileUploadUtil.saveFile(
                        fileBytes,
                        originalFileName,
                        ticket.getTicketId(),
                        moduleName,
                        ticket.getSubject()
                );
                DmsUploadResult uploadResult = uploadDocToDms(file, ticket.getTicketId());
                DmsDocument dmsDocument = new DmsDocument();
                dmsDocument.setDocumentId(uploadResult.getDocumentId());
                dmsDocument.setApplicationId(ticket.getTicketId());
                dmsDocument.setFileName(originalFileName);
                dmsDocument.setOriginalFileName(originalFileName);
                dmsDocument.setFileType(file.getContentType());
                dmsDocument.setFileSizeBytes(file.getSize());
                dmsDocument.setContentHash(DigestUtils.sha256Hex(fileBytes));
                dmsDocument.setFilePath(savedPath);
                dmsDocument.setIsDeleted(false);
                DmsDocument savedDmsDocument = dmsDocumentRepository.save(dmsDocument);
                if (uploadResult.getMetadata() != null && !uploadResult.getMetadata().isEmpty()) {
                    uploadResult.getMetadata().forEach((key, value) -> {
                        DmsDocumentMetadata metadata = new DmsDocumentMetadata();
                        metadata.setDocument(savedDmsDocument);
                        metadata.setMetadataKey(key);
                        metadata.setMetadataValue(value);
                        dmsDocumentMetadataRepository.save(metadata);
                    });
                }
                TicketAttachment attachment = new TicketAttachment();
                attachment.setTicket(ticket);
                attachment.setDmsDocument(savedDmsDocument);
                attachment.setFileName(originalFileName);
                attachment.setFilePath(savedPath);
                attachment.setFileSizeKB((int) (file.getSize() / 1024));
                attachment.setContentType(file.getContentType());
                attachment.setUploadedBy(uploadedBy);
                attachment.setUploadedOn(LocalDateTime.now());
                attachments.add(attachment);

            } catch (Exception ex) {
                log.error("DMS upload failed for file {} RequestId={}", originalFileName, ticketRequestDto, ex);
                saveDmsErrorLog(ticket.getTicketId(), ticketRequestDto, ex);
                throw ex;
            }
        }
        return ticketAttachmentRepository.saveAll(attachments);
    }

    private DmsUploadResult uploadDocToDms(MultipartFile file, Long ticketId) {
        try {
            log.info("Uploading document to DMS. TicketId={}", ticketId);
            Object response = documentClientService.uploadDocument(
                    file,
                    file.getOriginalFilename(),
                    String.valueOf(ticketId),
                    new DocumentServiceCallback() {
                        @Override
                        public void onSuccess(DmsResponseDTO<?> dmsResponseDTO) {
                            log.info("DMS upload success callback. TicketId={}", ticketId);
                        }
                        @Override
                        public void onFailure(DmsResponseDTO<?> dmsResponseDTO) {
                            log.error("DMS upload failed callback. TicketId={}", ticketId);
                        }
                    }
            );
            String docIdStr = extractDocumentId(response);
            if (docIdStr == null) {
                throw new RuntimeException("DocumentId not found in DMS response. TicketId=" + ticketId);
            }
            Long documentId = Long.valueOf(docIdStr);
            Map<String, String> metadata = new HashMap<>();
            if (response instanceof DmsResponseDTO) {
                Object dataObj = ((DmsResponseDTO<?>) response).getData();
                if (dataObj instanceof Map) {
                    Object metaObj = ((Map<?, ?>) dataObj).get("metadata");
                    if (metaObj instanceof Map) {
                        ((Map<?, ?>) metaObj).forEach((k, v) -> metadata.put(String.valueOf(k), String.valueOf(v)));
                    }
                }
            }
            log.info("Document uploaded successfully. TicketId={}, DocumentId={}", ticketId, documentId);
            return new DmsUploadResult(documentId, metadata);
        } catch (Exception ex) {
            log.error("DMS upload failed for TicketId={}", ticketId, ex);
            throw new RuntimeException("Failed to upload document to DMS. TicketId=" + ticketId, ex);
        }
    }


    @SuppressWarnings("unchecked")
    private String extractDocumentId(Object response) {
        if (response instanceof DmsResponseDTO) {
            DmsResponseDTO dmsResponse = (DmsResponseDTO) response;

            Object dataObj = dmsResponse.getData();
            if (dataObj instanceof Map) {
                Map<String, Object> dataMap = (Map<String, Object>) dataObj;
                Object documentId = dataMap.get("documentId");
                if (documentId != null) {
                    return String.valueOf(documentId);
                }
            }

            log.warn("DMS response does not contain documentId. Response: {}", dmsResponse);
            return null;
        }
        return null;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveDmsErrorLog(
            Long ticketId,
            String requestPayload,
            Exception ex) {
        DmsErrorLog errorLog = new DmsErrorLog();
        errorLog.setApplicationId(ticketId);
        errorLog.setClassName(this.getClass().getSimpleName());
        errorLog.setMessage(ex.getMessage());
        errorLog.setRequestId(requestPayload);
        errorLog.setStackTrace(Arrays.toString(ex.getStackTrace()));
        errorLog.setTimestamp(LocalDateTime.now());

        dmsErrorLogRepository.save(errorLog);
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
        return new CombinedTicketResponseDto(
                ticketMapper.toDto(ticket),
                ticketTagMapper.mapTagsListToDtoList(tags),
                ticketCcMapper.mapTagsListToDtoList(ccs),
                ticketAttachmentMapper.mapTagsListToDtoList(attachments)
        );
    }

}