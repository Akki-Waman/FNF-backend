package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ClientProductController;
import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientProductControllerImpl implements ClientProductController {

    private final ClientProductService clientProductService;

    @Override
    public ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> saveClientProducts(ClientProductsRequestDTO clientProductsRequestDTO) {
        log.info("<<Start>> saveClientProducts endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> response =
                ResponseEntity.ok(clientProductService.saveClientProducts(clientProductsRequestDTO));
        log.info("<<End>> saveClientProducts endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> updateClientProducts( Long clientProductId, ClientProductsRequestDTO clientProductsRequestDTO) {
        log.info("<<Start>> updateClientProducts endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> response =
                ResponseEntity.ok(clientProductService.updateClientProducts(clientProductId,clientProductsRequestDTO));
        log.info("<<End>> updateClientProducts endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> deleteClientProducts(Long clientProductId) {
        log.info("<<Start>> deleteClientProducts endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> response =
                ResponseEntity.ok(clientProductService.deleteClientProducts(clientProductId));
        log.info("<<End>> deleteClientProducts endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>>> getAllClientProducts() {
        log.info("<<Start>>getAllClientProducts endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>>> response =
                ResponseEntity.ok(clientProductService.getAllClientProducts());
        log.info("<<End>>getAllClientProducts endpoint called<<End>>");
        return response;
    }
}