package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {

    ApiResponseDTO<ContactResponseDto> saveContact(
            ContactRequestDto contactRequestDto
    );

    ApiResponseDTO<ContactResponseDto> updateContact(
            ContactRequestDto contactRequestDto
    );

    ApiResponseDTO<ContactResponseDto> getById(
            Long contactId
    );

    ApiResponseDTO<String> deleteById(
            Long contactId
    );

    ApiResponseDTO<PagedResponse<ContactResponseDto>> getAllContacts();
}
