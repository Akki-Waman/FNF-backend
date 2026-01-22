package com.sipl.ticket.service.impl;


import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Companies;
import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dao.repository.CompanyRepository;
import com.sipl.ticket.core.dao.repository.ServiceRepository;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.request.ServiceSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.ServiceExcelGenerator;
import com.sipl.ticket.core.mapper.ServiceMapper;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;
    private final ServiceMapper mapper;
    private final CompanyRepository companyRepository;

    @Override
    @CacheEvict(value = "services", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "SERVICE",
            description = "Service {0} created successfully"
    )
    public ApiResponseDTO<ServiceResponseDTO> saveService(ServiceRequestDto dto) {

        try {

            Companies company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            if (repository.existsActiveServiceForCompany(
                    dto.getServiceName(), dto.getCompanyId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Service '" + dto.getServiceName() + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            ServiceEntity service = mapper.toEntity(dto);
            service.setCompany(company);
            service.setIsActive(true);
            service.setIsDelete(false);

            ServiceEntity saved = repository.save(service);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Service created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveService error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while creating service",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "services", allEntries = true)
    @ActivityLoggable(
            action = "UPDATE",
            module = "SERVICE",
            description = "Service {0} updated successfully"
    )
    public ApiResponseDTO<ServiceResponseDTO> updateService(ServiceRequestDto dto) {

        if (dto == null || dto.getServiceId() == null) {
            throw new IllegalArgumentException("Service ID is required");
        }

        ServiceEntity service = repository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        EntityStateValidator.checkNotDeleted(
                service.getIsDelete(),
                "Service",
                service.getServiceName()
        );

        boolean isUpdated = false;

        if (dto.getServiceName() != null && !dto.getServiceName().trim().isEmpty()) {
            String name = dto.getServiceName().trim();

            if (repository.existsByServiceNameIgnoreCaseAndServiceIdNot(
                    name, dto.getServiceId())) {
                throw new IllegalStateException("Service '" + name + "' already exists");
            }

            service.setServiceName(name);
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            service.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }

        ServiceEntity saved = repository.save(service);

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "Service updated successfully",
                HttpStatus.OK,
                false
        );
    }



    @Override
    @Cacheable(value = "services", key = "#serviceId")
    public ApiResponseDTO<ServiceResponseDTO> getById(Long serviceId) {

        return repository.findById(serviceId)
                .filter(s ->
                        Boolean.TRUE.equals(s.getIsActive()) &&
                                Boolean.FALSE.equals(s.getIsDelete())
                )
                .map(s -> new ApiResponseDTO<>(
                        mapper.toDto(s),
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
    }

    @Override
    @CacheEvict(value = "services", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "SERVICE",
            description = "Service id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long serviceId) {

        ServiceEntity service = repository.findById(serviceId).orElse(null);

        if (service == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Service not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }
        if (Boolean.TRUE.equals(service.getIsDelete())) {
            return new ApiResponseDTO<>(
                    null,
                    "Service already deleted",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }
        service.setIsActive(false);
        service.setIsDelete(true);
        repository.save(service);

        return new ApiResponseDTO<>(
                null,
                "Service deleted successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<PagedResponse<ServiceResponseDTO>> searchServices(
            ServiceSearchRequestDto dto) {

        Pageable pageable = PaginationUtil.pageable(
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir()
        );

        Page<ServiceEntity> pageResult =
                repository.searchServices(
                        dto.getQuery(),
                        dto.getIsActive(),
                        dto.getCompanyId(),
                        pageable
                );

        if (pageResult.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No services found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        List<ServiceResponseDTO> content = pageResult.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "Services fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "services")
    public ApiResponseDTO<ServiceResponseDTO> getAllServices() {

        try {
            List<ServiceResponseDTO> list = repository
                    .findAll(Sort.by(Sort.Direction.DESC, "serviceId"))
                    .stream()
                    .filter(s ->
                            Boolean.TRUE.equals(s.getIsActive()) &&
                                    Boolean.FALSE.equals(s.getIsDelete())
                    )
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No services found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Services fetched successfully",
                    false,
                    null
            );

        } catch (Exception e) {
            log.error("getAllServices error", e);
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
    public void exportServicesExcel(HttpServletResponse response) {

        try {
            List<ServiceResponseDTO> services = repository.findAll()
                    .stream()
                    .filter(s ->
                            Boolean.TRUE.equals(s.getIsActive()) &&
                                    Boolean.FALSE.equals(s.getIsDelete())
                    )
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            ServiceExcelGenerator.generateExcel(services, response);

        } catch (Exception e) {
            log.error("exportServicesExcel error", e);
            throw new RuntimeException("Failed to export services Excel", e);
        }
    }
}

