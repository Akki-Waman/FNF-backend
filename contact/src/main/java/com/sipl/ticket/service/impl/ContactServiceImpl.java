package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dao.repository.ContactRepository;
import com.sipl.ticket.core.dao.repository.DepartmentRepository;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.ContactMapper;
import com.sipl.ticket.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final ContactMapper contactMapper;

    /* ================= SAVE ================= */

    @Override
    public ApiResponseDTO<ContactResponseDto> saveContact(ContactRequestDto dto) {

        log.info("<<Start>> saveContact called <<Start>>");

        try {
            if (dto == null || dto.getContactName() == null || dto.getContactName().trim().isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact name is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElse(null);

            if (department == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Department not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Contact contact = contactMapper.toEntity(dto);
            contact.setDepartment(department);
            contact.setIsActive(true);

            Contact saved = contactRepository.save(contact);

            return new ApiResponseDTO<>(
                    contactMapper.toResponseDto(saved),
                    "Contact created successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("saveContact error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }

    }

    /* ================= UPDATE ================= */

    @Override
    public ApiResponseDTO<ContactResponseDto> updateContact(ContactRequestDto dto) {

        log.info("<<Start>> updateContact called <<Start>>");

        try {
            if (dto == null || dto.getContactId() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact ID is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Contact contact = contactRepository.findById(dto.getContactId())
                    .orElse(null);

            if (contact == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElse(null);

            if (department == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Department not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            contactMapper.partialUpdate(dto, contact);
            contact.setDepartment(department);

            Contact updated = contactRepository.save(contact);

            return new ApiResponseDTO<>(
                    contactMapper.toResponseDto(updated),
                    "Contact updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateContact error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ================= GET BY ID ================= */

    @Override
    public ApiResponseDTO<ContactResponseDto> getById(Long contactId) {

        log.info("<<Start>> getById called <<Start>>");

        try {
            return contactRepository.findById(contactId)
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
            log.error("getById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ================= DELETE (SOFT) ================= */

    @Override
    public ApiResponseDTO<String> deleteById(Long contactId) {

        log.info("<<Start>> deleteById called <<Start>>");

        try {
            Contact contact = contactRepository.findById(contactId)
                    .orElse(null);

            if (contact == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(contact.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Contact already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            contact.setIsActive(false);
            contactRepository.save(contact);

            return new ApiResponseDTO<>(
                    null,
                    "Contact deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ================= GET ALL ================= */

    @Override
    public ApiResponseDTO<PagedResponse<ContactResponseDto>> getAllContacts() {

        log.info("<<Start>> getAllContacts called <<Start>>");

        try {
            List<Contact> contacts = contactRepository.findAll();

            if (contacts.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No contacts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ContactResponseDto> response =
                    contactMapper.toResponseDtoList(contacts);

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
            log.error("getAllContacts error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}

