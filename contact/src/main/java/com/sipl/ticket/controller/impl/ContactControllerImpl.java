package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ContactController;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.request.ContactSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContactControllerImpl implements ContactController {

    private final ContactService contactService;

    @Override
    public ResponseEntity<ApiResponseDTO<ContactResponseDto>> saveContact(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        log.info("<<Start>> saveContact endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ContactResponseDto>> response =
                ResponseEntity.ok(contactService.saveContact(contactRequestDto));
        log.info("<<End>> saveContact endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ContactResponseDto>> updateContact( @Valid @RequestBody ContactRequestDto contactRequestDto) {
        log.info("<<Start>> updateContact endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ContactResponseDto>> response =
                ResponseEntity.ok(contactService.updateContact(contactRequestDto));
        log.info("<<End>> updateContact endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ContactResponseDto>> getById(Long contactId) {
        log.info("<<Start>> getById endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ContactResponseDto>> response =
                ResponseEntity.ok(contactService.getById(contactId));
        log.info("<<End>> getById endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Long contactId) {
        log.info("<<Start>> deleteById endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(contactService.deleteById(contactId));
        log.info("<<End>> deleteById endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> getAllContacts(Integer branchId) {
        log.info("<<Start>> getAllContacts endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> response =
                ResponseEntity.ok(contactService.getAllContacts(branchId));
        log.info("<<End>> getAllContacts endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> searchContacts(
            @RequestBody ContactSearchRequestDto searchRequestDto
    ) {
        log.info("<<Start>> searchContacts  <<Start>>");

        ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> response =
                ResponseEntity.ok(
                        contactService.searchContacts(searchRequestDto)
                );

        log.info("<<End>> searchContacts <<End>>");

        return response;
    }

    @Override
    public ResponseEntity<Void> exportContactsExcel(HttpServletResponse response) {

        log.info("<<Start>> exportContactsExcel <<Start>>");

        contactService.exportContactsExcel(response);

        log.info("<<End>> exportContactsExcel <<End>>");

        return ResponseEntity.ok().build();
    }

}
