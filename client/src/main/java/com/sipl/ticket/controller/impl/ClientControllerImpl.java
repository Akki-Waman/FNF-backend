package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ClientController;
import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SearchClientRequestDto;
import com.sipl.ticket.service.ClientService;
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
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;

    @Override
    public ResponseEntity<ApiResponseDTO<ClientResponseDto>> saveClient(
            @Valid @RequestBody ClientRequestDto dto) {

        log.info("<<Start>> saveClient endpoint called <<Start>>");

        ApiResponseDTO<ClientResponseDto> response =
                clientService.saveClient(dto);

        log.info("<<End>> saveClient endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ClientResponseDto>> updateClient(
            @Valid @RequestBody ClientRequestDto dto) {

        log.info("<<Start>> updateClient endpoint called <<Start>>");

        ApiResponseDTO<ClientResponseDto> response =
                clientService.updateClient(dto);

        log.info("<<End>> updateClient endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ClientResponseDto>>> searchClient(
            @Valid @RequestBody SearchClientRequestDto searchDto) {

        log.info("<<Start>> searchClient endpoint called <<Start>>");

        ApiResponseDTO<PagedResponse<ClientResponseDto>> response =
                clientService.searchClient(searchDto);

        log.info("<<End>> searchClient endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ClientResponseDto>> getById(
            Long clientId) {

        log.info("<<Start>> getById endpoint called <<Start>>");

        ApiResponseDTO<ClientResponseDto> response =
                clientService.getById(clientId);

        log.info("<<End>> getById endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long clientId) {

        log.info("<<Start>> deleteById endpoint called <<Start>>");

        ApiResponseDTO<String> response =
                clientService.deleteById(clientId);

        log.info("<<End>> deleteById endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ClientResponseDto>>> getAllClients() {

        log.info("<<Start>> getAllClients endpoint called <<Start>>");

        ApiResponseDTO<PagedResponse<ClientResponseDto>> response =
                clientService.getAllClients();

        log.info("<<End>> getAllClients endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    public ResponseEntity<Void> exportClientsExcel(HttpServletResponse response) {

        log.info("<<Start>> exportClientsExcel endpoint called <<Start>>");

        clientService.exportClientsExcel(response);

        log.info("<<End>> exportClientsExcel endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }
}
