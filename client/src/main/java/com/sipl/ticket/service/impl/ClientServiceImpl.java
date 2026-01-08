package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Client;
import com.sipl.ticket.core.dao.repository.ClientRepository;
import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SearchClientRequestDto;
import com.sipl.ticket.core.helper.ClientExcelGenerator;
import com.sipl.ticket.core.mapper.ClientMapper;
import com.sipl.ticket.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    @ActivityLoggable(action = "CREATE", module = "CLIENT")
    public ApiResponseDTO<ClientResponseDto> saveClient(ClientRequestDto dto) {

        log.info("Saving client with code={}, name={}", dto.getClientCode(), dto.getClientName());

        try {
            Client client = mapper.toEntity(dto);
            client.setIsActive(true);

            Client saved = repository.save(client);

            log.info("Client created successfully, id={}", saved.getClientId());

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Client created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveClient unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    @ActivityLoggable(action = "UPDATE", module = "CLIENT")
    public ApiResponseDTO<ClientResponseDto> updateClient(ClientRequestDto dto) {

        log.info("Updating client id={}", dto.getClientId());

        try {
            Client client = repository.findById(dto.getClientId()).orElse(null);

            if (client == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Client not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            client.setClientCode(dto.getClientCode());
            client.setClientName(dto.getClientName());

            Client updated = repository.save(client);

            log.info("Client updated successfully, id={}", updated.getClientId());

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Client updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateClient unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ClientResponseDto> getById(Long id) {

        log.info("Fetching client by id={}", id);

        try {
            return repository.findById(id)
                    .map(client -> new ApiResponseDTO<>(
                            mapper.toDto(client),
                            "Client found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Client not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "clients", allEntries = true)
    @ActivityLoggable(action = "DELETE", module = "CLIENT")
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deleting client id={}", id);

        try {
            Client client = repository.findById(id).orElse(null);

            if (client == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Client not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(client.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Client already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            client.setIsActive(false);
            repository.save(client);

            log.info("Client deleted successfully, id={}", id);

            return new ApiResponseDTO<>(
                    null,
                    "Client deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<ClientResponseDto>> searchClient(
            SearchClientRequestDto dto) {

        log.info("<<Start>> searchClient endpoint called <<Start>>");

        try {
            List<Client> clients = repository.searchClients(
                    dto.getClientCode(),
                    dto.getClientName()
            );

            if (clients == null || clients.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No clients found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ClientResponseDto> responseList = mapper.toDtoList(clients);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(responseList, 0, responseList.size(), 1, responseList.size(), true),
                    "Clients fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchClient unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("clients")
    public ApiResponseDTO<PagedResponse<ClientResponseDto>> getAllClients() {

        log.info("Fetching all clients");

        try {
            List<Client> list = repository.findAll();

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No clients found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ClientResponseDto> response = mapper.mapClientListToDtoList(list);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(response, 0, response.size(), 1, response.size(), true),
                    "Clients fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllClients unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public void exportClientsExcel(HttpServletResponse response) {

        log.info("Exporting active clients to Excel");

        try {
            List<ClientResponseDto> clients = repository.findAll()
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            ClientExcelGenerator.generateExcel(clients, response);

            log.info(
                    "Clients Excel export completed successfully, totalRecords={}",
                    clients.size()
            );

        } catch (Exception e) {
            log.error("exportClientsExcel unexpected error", e);
            throw new RuntimeException("Failed to export clients Excel", e);
        }
    }
}
