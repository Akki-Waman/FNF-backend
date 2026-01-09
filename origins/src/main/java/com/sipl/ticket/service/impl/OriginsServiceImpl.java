package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Origins;
import com.sipl.ticket.core.dao.repository.OriginsRepository;
import com.sipl.ticket.core.dto.request.OriginsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OriginDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.OriginsExcelGenerator;
import com.sipl.ticket.core.mapper.OriginsMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.OriginsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sipl.ticket.core.dto.request.OriginSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OriginsServiceImpl implements OriginsService {

    private final OriginsRepository repository;
    private final OriginsMapper mapper;

    @Override
    @CacheEvict(value = "origins", allEntries = true)
    @ActivityLoggable(action = "CREATE", module = "ORIGIN")
    public ApiResponseDTO<OriginDto> saveOrigin(OriginsRequestDto dto) {

        log.info("Request received to save Origin");

        if (dto == null || dto.getOriginName() == null) {
            log.warn("Invalid request: Origin request or origin name is null");
            return new ApiResponseDTO<>(
                    null,
                    "Origin name is required",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        log.debug("Raw origin name received: '{}'", dto.getOriginName());

        try {
            String name = dto.getOriginName().trim();
            log.debug("Trimmed origin name: '{}'", name);

            log.debug("Checking if origin already exists for name: '{}'", name);
            boolean exists = repository.findActiveByOriginName(name)
                    .stream()
                    .anyMatch(o -> o.getOriginName().equalsIgnoreCase(name));

            if (exists) {
                log.warn("Origin already exists with name: '{}'", name);
                return new ApiResponseDTO<>(
                        null,
                        "Origin '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Origins origin = new Origins();
            origin.setOriginName(name);
            origin.setIsActive(true);

            log.info("Saving new origin with name: '{}'", name);
            Origins saved = repository.save(origin);

            log.info("Origin saved successfully with ID: {} and name: '{}'",
                    saved.getOriginId(), saved.getOriginName());

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Origin created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving origin. Request data: {}", dto, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "origins", allEntries = true)
    @ActivityLoggable(action = "UPDATE", module = "ORIGIN")
    public ApiResponseDTO<OriginDto> updateOrigin(OriginsRequestDto dto) {

        log.info("Updating origin, id={}, name={}", dto.getOriginId(), dto.getOriginName());

        try {
            if (dto == null || dto.getOriginId() == null ||
                    dto.getOriginName() == null || dto.getOriginName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Origin ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Origins origin = repository.findById(dto.getOriginId()).orElse(null);

            if (origin == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Origin not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(origin.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive origin cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getOriginName().trim();

            boolean exists = repository.findActiveByOriginName(name)
                    .stream()
                    .anyMatch(o ->
                            o.getOriginName().equalsIgnoreCase(name)
                                    && !o.getOriginId().equals(dto.getOriginId())
                    );

            if (exists) {
                return new ApiResponseDTO<>(
                        null,
                        "Origin with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            origin.setOriginName(name);
            Origins updated = repository.save(origin);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Origin updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateOrigin unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<OriginDto> getById(Long id) {

        log.info("Fetching origin by id={}", id);

        try {
            return repository.findById(id)
                    .filter(o -> Boolean.TRUE.equals(o.getIsActive()))
                    .map(o -> new ApiResponseDTO<>(
                            mapper.toDto(o),
                            "Origin found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Origin not found",
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
    @CacheEvict(value = "origins", allEntries = true)
    @ActivityLoggable(action = "DELETE", module = "ORIGIN")
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating origin, id={}", id);

        try {
            Origins origin = repository.findById(id).orElse(null);

            if (origin == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Origin not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(origin.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Origin is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            origin.setIsActive(false);
            repository.save(origin);

            return new ApiResponseDTO<>(
                    null,
                    "Origin deleted successfully",
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
    @Cacheable("origins")
    public ApiResponseDTO<OriginDto> getAllOrigins() {

        log.info("Fetching all active origins");

        try {
            List<OriginDto> list = repository.findAll()
                    .stream()
                    .filter(o -> Boolean.TRUE.equals(o.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No origins found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Origins fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllOrigins error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponse<OriginDto>> searchOrigins(
            OriginSearchRequestDto dto) {

        log.info("<<START>> searchOrigins service called");


        try {

            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Origins> pageResult = repository.searchOrigins(
                    dto.getQuery(),
                    dto.getIsActive(),
                    pageable
            );

            log.info("DB result | totalElements={}, totalPages={}",
                    pageResult.getTotalElements(),
                    pageResult.getTotalPages());

            if (pageResult.isEmpty()) {
                log.warn("No origins found for originId");

                return new ApiResponseDTO<>(
                        null,
                        "No origins found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<OriginDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            log.info("Records mapped | count={}", content.size());

            PagedResponse<OriginDto> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            log.info("<<END>> searchOrigins service completed successfully");

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Origins fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error in searchOrigins ", e);

            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public void downloadExcel(HttpServletResponse response) {

        log.info("<<START>> Download Origins Excel service");

        try {
            List<OriginDto> dtoList = repository.findAll()
                    .stream()
                    .filter(o -> Boolean.TRUE.equals(o.getIsActive()))
                    .map(o -> {
                        OriginDto dto = new OriginDto();


                        dto.setOriginId(o.getOriginId());
                        dto.setOriginName(o.getOriginName());
                        dto.setIsActive(o.getIsActive());


                        dto.setCreatedBy(
                                o.getCreatedBy() != null
                                        ? o.getCreatedBy().getUserName()
                                        : null
                        );

                        dto.setCreatedTime(o.getCreatedTime());

                        dto.setModifiedBy(
                                o.getModifiedBy() != null
                                        ? o.getModifiedBy().getUserName()
                                        : null
                        );

                        dto.setModifiedTime(o.getModifiedTime());

                        return dto;
                    })
                    .collect(Collectors.toList());

            log.info("Total active origins fetched for Excel download: {}",
                    dtoList.size());

            OriginsExcelGenerator.generateExcel(dtoList, response);

            log.info("Origins Excel generated successfully");

        } catch (IOException e) {
            log.error("IOException occurred while downloading Origins Excel", e);
            throw new RuntimeException("Failed to download Origins Excel");
        } catch (Exception e) {
            log.error("Unexpected error occurred while downloading Origins Excel", e);
            throw new RuntimeException("Failed to download Origins Excel");
        }

        log.info("<<END>> Download Origins Excel service");
    }


}
