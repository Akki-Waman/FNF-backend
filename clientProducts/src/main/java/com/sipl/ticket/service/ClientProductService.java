package com.sipl.ticket.service;


import com.sipl.ticket.core.dto.request.ClientProductSearchRequestDto;
import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ClientProductService {
    ApiResponseDTO<ClientProductsResponseDTO> saveClientProducts(ClientProductsRequestDTO clientProductsRequestDTO);

    ApiResponseDTO<ClientProductsResponseDTO> updateClientProducts(Long clientProductId,ClientProductsRequestDTO clientProductsRequestDTO);

    ApiResponseDTO<ClientProductsResponseDTO> deleteClientProducts(Long clientProductId);

    ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>> getAllClientProducts(Integer branchId);

    ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>> searchClientProducts(
            ClientProductSearchRequestDto requestDto);
    void exportClientProductsExcel(HttpServletResponse response);

    ApiResponseDTO<ClientProductsResponseDTO> getById(
            Long clientProductId
    );

    void exportClientProducts(ClientProductsRequestDTO requestDto, String format, HttpServletResponse response);
}