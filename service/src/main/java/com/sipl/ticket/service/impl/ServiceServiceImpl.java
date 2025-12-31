package com.sipl.ticket.service.impl;


import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dao.repository.ServiceRepository;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import com.sipl.ticket.core.helper.ServiceExcelGenerator;
import com.sipl.ticket.core.mapper.ServiceMapper;
import com.sipl.ticket.service.ServiceService;
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
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;
    private final ServiceMapper mapper;

    @Override
    @CacheEvict(value = "services", allEntries = true)
    public ApiResponseDTO<ServiceResponseDTO> saveService(ServiceRequestDto dto) {

        log.info("Saving service with name: {}", dto.getServiceName());

        try {
            String name = dto.getServiceName().trim();

            if (repository.existsByServiceNameIgnoreCaseAndIsDeletedFalse(name)) {
                log.warn("Service already exists with name: {}", name);
                return new ApiResponseDTO<>(
                        null,
                        "Service '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            ServiceEntity service = mapper.toEntity(dto);
            service.setServiceName(name);
            service.setIsActive(true);
            service.setIsDeleted(false);

            ServiceEntity savedService = repository.save(service);

            log.info("Service created successfully with id: {}", savedService.getServiceId());

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(savedService),
                    "Service created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving service", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "services", allEntries = true)
    public ApiResponseDTO<ServiceResponseDTO> updateService(ServiceRequestDto dto) {

        log.info("Updating service, id={}, name={}", dto.getServiceId(), dto.getServiceName());

        try {
            if (dto == null || dto.getServiceId() == null ||
                    dto.getServiceName() == null || dto.getServiceName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Service ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            ServiceEntity service = repository.findById(dto.getServiceId())
                    .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                    .orElse(null);

            if (service == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Service not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            String name = dto.getServiceName().trim();

            if (repository.existsByServiceNameIgnoreCaseAndServiceIdNotAndIsDeletedFalse(
                    name, dto.getServiceId())) {

                log.warn("Duplicate service name '{}' found while updating", name);

                return new ApiResponseDTO<>(
                        null,
                        "Service with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            service.setServiceName(name);
            ServiceEntity updated = repository.save(service);

            log.info("Service updated successfully, id={}", updated.getServiceId());

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(updated),
                    "Service updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateService unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ServiceResponseDTO> getById(Long id) {

        log.info("Fetching service by id={}", id);

        try {
            return repository.findById(id)
                    .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                    .map(s -> new ApiResponseDTO<>(
                            mapper.toResponseDto(s),
                            "Service found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Service not found",
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
    @CacheEvict(value = "services", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deleting service, id={}", id);

        try {
            ServiceEntity service = repository.findById(id).orElse(null);

            if (service == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Service not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.TRUE.equals(service.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Service is already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            service.setIsActive(false);
            service.setIsDeleted(true);
            repository.save(service);

            log.info("Service deleted successfully, id={}", id);

            return new ApiResponseDTO<>(
                    null,
                    "Service deleted successfully",
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
    @Cacheable("services")
    public ApiResponseDTO<PagedResponse<ServiceResponseDTO>> getAllServices() {

        log.info("Fetching all services");

        try {
            List<ServiceEntity> list = repository.findByIsDeletedFalse();

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No services found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ServiceResponseDTO> response = mapper.toResponseDtoList(list);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(response, 0, response.size(), 1, response.size(), true),
                    "Services fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllServices unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public void exportServicesExcel(HttpServletResponse response) {

        log.info("Exporting active services to Excel");

        try {
            List<ServiceResponseDTO> services = repository.findAll()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .map(s -> new ServiceResponseDTO(
                            s.getServiceId(),
                            s.getServiceName(),s.getIsActive(),s.getIsDeleted()
                    ))
                    .collect(Collectors.toList());

            ServiceExcelGenerator.generateExcel(services, response);

            log.info(
                    "Services Excel export completed successfully, totalRecords={}",
                    services.size()
            );

        } catch (Exception e) {
            log.error("exportServicesExcel unexpected error", e);
            throw new RuntimeException("Failed to export services Excel", e);
        }
    }
}
