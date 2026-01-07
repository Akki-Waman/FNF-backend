package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SearchClientRequestDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ClientService {


    ApiResponseDTO<ClientResponseDto> saveClient(
            ClientRequestDto clientRequestDto
    );


    ApiResponseDTO<ClientResponseDto> updateClient(
            ClientRequestDto clientRequestDto
    );


    ApiResponseDTO<ClientResponseDto> getById(
            Long clientId
    );


    ApiResponseDTO<String> deleteById(
            Long clientId
    );


    ApiResponseDTO<PagedResponse<ClientResponseDto>> searchClient(
            SearchClientRequestDto searchClientRequestDto
    );


    ApiResponseDTO<PagedResponse<ClientResponseDto>> getAllClients();

    void exportClientsExcel(HttpServletResponse response);

}
