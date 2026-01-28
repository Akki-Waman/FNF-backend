package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dao.repository.BranchRepository;
import com.sipl.ticket.core.dao.repository.ContactRepository;
import com.sipl.ticket.core.dao.repository.DepartmentRepository;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.request.ContactSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.ContactExcelGenerator;
import com.sipl.ticket.core.mapper.ContactMapper;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.sipl.ticket.core.util.PaginationUtil;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final ContactMapper contactMapper;
    private final BranchRepository branchRepository;


    @Override
    @ActivityLoggable(
            action = "CREATE",
            module = "CONTACT",
            description = "Contact {0} created successfully"
    )
    public ApiResponseDTO<ContactResponseDto> saveContact(ContactRequestDto dto) {

        log.info(
                "Creating contact [name={}, email={}, mobile={}]",
                dto != null ? dto.getContactName() : null,
                dto != null ? dto.getEmailAddress() : null,
                dto != null ? dto.getMobileNo() : null
        );

        try {

            String name = dto.getContactName().trim();

            Integer branchId = dto.getBranchId().intValue();

            Branches branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new RuntimeException("Branch not found"));

            if (contactRepository.existsActiveContactInDepartment(name, branchId)) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Contact contact = contactMapper.toEntity(dto);
            contact.setBranch(branch);
            contact.setIsActive(true);
            contact.setIsDelete(false);

            Contact saved = contactRepository.save(contact);

            return new ApiResponseDTO<>(
                    contactMapper.toResponseDto(saved),
                    "Contact created successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Unexpected error while creating contact", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }



    @Override
    public ApiResponseDTO<ContactResponseDto> updateContact(ContactRequestDto dto) {

        if (dto == null || dto.getContactId() == null) {
            throw new IllegalArgumentException("Contact ID is required");
        }

        Contact contact = contactRepository.findById(dto.getContactId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact"));

        if (Boolean.TRUE.equals(contact.getIsDelete())) {
            throw new IllegalStateException("Contact is deleted");
        }

        boolean isUpdated = false;

        if (dto.getContactName() != null && !dto.getContactName().isBlank()) {
            contact.setContactName(dto.getContactName().trim());
            isUpdated = true;
        }

        if (dto.getEmailAddress() != null && !dto.getEmailAddress().isBlank()) {
            contact.setEmailAddress(dto.getEmailAddress().trim());
            isUpdated = true;
        }

        if (dto.getMobileNo() != null && !dto.getMobileNo().isBlank()) {
            contact.setMobileNo(dto.getMobileNo().trim());
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            contact.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }

        Contact updated = contactRepository.save(contact);

        return new ApiResponseDTO<>(
                contactMapper.toResponseDto(updated),
                "Contact updated successfully",
                HttpStatus.OK,
                false
        );
    }


    @Override
    public ApiResponseDTO<ContactResponseDto> getById(Long contactId) {

        log.info("Fetching contact by id={}", contactId);

        try {
            return contactRepository.findById(contactId)
                    .filter(c ->
                            Boolean.TRUE.equals(c.getIsActive()) &&
                                    Boolean.FALSE.equals(c.getIsDelete())
                    )
                    .map(contact -> new ApiResponseDTO<>(
                            contactMapper.toResponseDto(contact),
                            "Contact found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Contact not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error(
                    "Unexpected error while fetching contact [id={}]",
                    contactId,
                    e
            );
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }



    @Override
    @ActivityLoggable(
            action = "DELETE",
            module = "CONTACT",
            description = "Contact id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long contactId) {

        log.info("Deactivating contact [id={}]", contactId);

        try {
            Contact contact = contactRepository.findById(contactId)
                    .orElse(null);

            if (contact == null) {
                log.warn("Contact delete failed – contact not found [id={}]", contactId);
                return new ApiResponseDTO<>(
                        null,
                        "Contact not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(contact.getIsActive())) {
                log.warn("Contact already inactive [id={}]", contactId);
                return new ApiResponseDTO<>(
                        null,
                        "Contact already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            if (Boolean.TRUE.equals(contact.getIsDelete())) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            contact.setIsDelete(true);
            contact.setIsActive(false);
            contactRepository.save(contact);

            log.info("Contact deactivated successfully [id={}]", contactId);

            return new ApiResponseDTO<>(
                    null,
                    "Contact deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error(
                    "Unexpected error while deleting contact [id={}]",
                    contactId,
                    e
            );
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }



    @Override
    public ApiResponseDTO<PagedResponse<ContactResponseDto>> getAllContacts(
            Integer branchId
    ) {

        log.info("Fetching contacts, branchId={}", branchId);

        try {
            List<Contact> contacts;

            if (branchId != null) {
                contacts = contactRepository
                        .findByBranch_BranchIdAndIsActiveTrueAndIsDeleteFalse(branchId);
            } else {
                contacts = contactRepository
                        .findByIsActiveTrueAndIsDeleteFalse();
            }

            if (contacts.isEmpty()) {
                log.warn("No contacts found");

                return new ApiResponseDTO<>(
                        null,
                        "No contacts found",
                        HttpStatus.OK,
                        false
                );
            }

            List<ContactResponseDto> response =
                    contactMapper.toResponseDtoList(contacts);

            log.info(
                    "Contacts fetched successfully, totalRecords={}",
                    response.size()
            );

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            response,
                            0,
                            response.size(),
                            1,
                            response.size(),
                            true
                    ),
                    "Contacts fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Unexpected error while fetching all contacts", e);

            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    public ApiResponseDTO<PagedResponse<ContactResponseDto>> searchContacts(
            ContactSearchRequestDto dto
    ) {

        log.info("Contact search initiated [request={}]", dto);

        try {

            String sortBy = dto.getSortBy();

            if ("departmentId".equalsIgnoreCase(sortBy)) {
                sortBy = "department.departmentId";
            } else if ("contactId".equalsIgnoreCase(sortBy)) {
                sortBy = "contactId";
            } else if ("contactName".equalsIgnoreCase(sortBy)) {
                sortBy = "contactName";
            }

            Sort sort = "asc".equalsIgnoreCase(dto.getSortDir())
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<Contact> pageResult =
                    contactRepository.searchContacts(
                            dto.getContactId(),
                            dto.getBranchId(),
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No contacts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ContactResponseDto> content =
                    pageResult.getContent()
                            .stream()
                            .map(contactMapper::toResponseDto)
                            .collect(Collectors.toList());

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    ),
                    "Contacts fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchContacts unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportContactsExcel(HttpServletResponse response) {

        log.info("Exporting active contacts to Excel");

        try {
            List<ContactResponseDto> contacts = contactRepository.findByIsActiveTrue()
                    .stream()
                    .filter(c ->
                            Boolean.TRUE.equals(c.getIsActive()) &&
                                    Boolean.FALSE.equals(c.getIsDelete())
                    )
                    .map(contactMapper::toResponseDto)
                    .collect(Collectors.toList());

            ContactExcelGenerator.generateExcel(contacts, response);

            log.info("Contacts Excel export completed successfully, totalRecords={}",
                    contacts.size());

        } catch (Exception e) {
            log.error("exportContactsExcel unexpected error", e);
            throw new RuntimeException("Failed to export contacts Excel", e);
        }
    }





}
