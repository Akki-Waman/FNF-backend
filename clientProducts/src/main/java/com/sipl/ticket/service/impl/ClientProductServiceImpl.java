package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    @Override
    public ApiResponseDTO<ClientProductsResponseDTO> saveClientProducts(ClientProductsRequestDTO clientProductsRequestDTO) {
        return null;
    }
}
