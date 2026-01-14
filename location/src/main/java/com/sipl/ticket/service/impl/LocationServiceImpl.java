package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Locations;
import com.sipl.ticket.core.dao.repository.LocationRepository;
import com.sipl.ticket.core.dto.request.LocationRequestDTO;
import com.sipl.ticket.core.dto.request.LocationSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.LocationExcelGenerator;
import com.sipl.ticket.core.mapper.LocationMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;
    private final LocationMapper mapper;

    /* ===================== SAVE ===================== */

    @Override
    @CacheEvict(value = "locations", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "LOCATION",
            description = "Location {0} created successfully"
    )
    public ApiResponseDTO<LocationResponseDTO> saveLocation(LocationRequestDTO dto) {

        try {
            String name = dto.getLocationName().trim();

            if (repository.existsByLocationNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Location '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Locations location = new Locations();
            location.setLocationName(name);
            location.setIsActive(true);
            location.setIsDeleted(false);

            Locations saved = repository.save(location);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Location created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveLocation error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== UPDATE ===================== */

    @Override
    @CacheEvict(value = "locations", allEntries = true)
    @ActivityLoggable(
            action = "UPDATE",
            module = "LOCATION",
            description = "Location {0} updated successfully"
    )
    public ApiResponseDTO<LocationResponseDTO> updateLocation(LocationRequestDTO dto) {

        try {
            if (dto == null ||
                    dto.getLocationId() == null ||
                    dto.getLocationName() == null ||
                    dto.getLocationName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Location ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Locations location =
                    repository.findById(dto.getLocationId()).orElse(null);

            if (location == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Location not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.TRUE.equals(location.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Deleted location cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            if (Boolean.FALSE.equals(location.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive location cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getLocationName().trim();

            if (repository.existsByLocationNameIgnoreCaseAndLocationIdNot(
                    name, dto.getLocationId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Location '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            location.setLocationName(name);

            Locations updated = repository.save(location);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Location updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateLocation error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== GET BY ID ===================== */

    @Override
    public ApiResponseDTO<LocationResponseDTO> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(l ->
                            Boolean.TRUE.equals(l.getIsActive()) &&
                                    Boolean.FALSE.equals(l.getIsDeleted())
                    )
                    .map(l -> new ApiResponseDTO<>(
                            mapper.toDto(l),
                            "Location found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Location not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getLocationById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== DELETE (SOFT DELETE) ===================== */

    @Override
    @CacheEvict(value = "locations", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "LOCATION",
            description = "Location id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long id) {

        try {
            Locations location = repository.findById(id).orElse(null);

            if (location == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Location not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.TRUE.equals(location.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Location already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            location.setIsActive(false);
            location.setIsDeleted(true);

            repository.save(location);

            return new ApiResponseDTO<>(
                    null,
                    "Location deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteLocation error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== GET ALL ===================== */

    @Override
    @Cacheable("locations")
    public ApiResponseDTO<PagedResponse<LocationResponseDTO>> getAllLocations() {

        try {
            List<LocationResponseDTO> list =
                    repository.findAll()
                            .stream()
                            .filter(l ->
                                    Boolean.TRUE.equals(l.getIsActive()) &&
                                            Boolean.FALSE.equals(l.getIsDeleted())
                            )
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No locations found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            list,
                            0,
                            list.size(),
                            1,
                            list.size(),
                            true
                    ),
                    "Locations fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllLocations error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== SEARCH ===================== */

    @Override
    public ApiResponseDTO<PagedResponse<LocationResponseDTO>> searchLocations(
            LocationSearchRequestDTO dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Locations> pageResult =
                    repository.searchLocations(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No locations found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<LocationResponseDTO> content =
                    pageResult.getContent()
                            .stream()
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            PagedResponse<LocationResponseDTO> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Locations fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchLocations error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ===================== EXPORT ===================== */

    @Override
    @Transactional(readOnly = true)
    public void exportLocationExcel(HttpServletResponse response) {

        log.info("Exporting active locations to Excel");

        try {
            List<LocationResponseDTO> locations =
                    repository.findAll()
                            .stream()
                            .filter(l ->
                                    Boolean.TRUE.equals(l.getIsActive()) &&
                                            Boolean.FALSE.equals(l.getIsDeleted())
                            )
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            LocationExcelGenerator.generateExcel(locations, response);

            log.info(
                    "Location Excel export completed | totalRecords={}",
                    locations.size()
            );

        } catch (Exception e) {
            log.error("exportLocationExcel error", e);
            throw new RuntimeException("Failed to export location Excel", e);
        }
    }
}
