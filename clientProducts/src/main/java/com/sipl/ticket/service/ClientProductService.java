package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ClientProductService {
    ApiResponseDTO<ClientProductsResponseDTO> saveClientProducts(ClientProductsRequestDTO clientProductsRequestDTO);
}
