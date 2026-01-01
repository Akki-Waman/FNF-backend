package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Locations;
import com.sipl.ticket.core.dao.repository.LocationRepository;
import com.sipl.ticket.core.dto.request.LocationRequestDTO;
import com.sipl.ticket.core.dto.request.LocationSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.LocationMapper;
import com.sipl.ticket.service.LocationService;
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
    public ApiResponseDTO<LocationResponseDTO> updateLocation(LocationRequestDTO locationRequestDTO)  {

        try {
            if ( locationRequestDTO == null ||
                    locationRequestDTO.getLocationName() == null ||
                    locationRequestDTO.getLocationName().trim().isEmpty()){

                return new ApiResponseDTO<>(
                        null,
                        "Location ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Locations location = repository.findById(locationRequestDTO.getLocationId()).orElse(null);

            if (location == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Location not found",
                        HttpStatus.NOT_FOUND,
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

            String name = locationRequestDTO.getLocationName().trim();

            if (repository.existsByLocationNameIgnoreCaseAndLocationIdNot(
                    name, locationRequestDTO.getLocationId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Location '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            location.setLocationName(name);
        //    location.setLocationType(locationRequestDTO.getLocationType());

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
                    .filter(l -> Boolean.TRUE.equals(l.getIsActive()))
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

    /* ===================== DELETE ===================== */

    @Override
    @CacheEvict(value = "locations", allEntries = true)
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

            if (Boolean.FALSE.equals(location.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Location already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            location.setIsActive(false);
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
            List<LocationResponseDTO> list = repository.findAll()
                    .stream()
                    .filter(l -> Boolean.TRUE.equals(l.getIsActive()))
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

    @Override
    public ApiResponseDTO<PagedResponse<LocationResponseDTO>> searchLocations(
            LocationSearchRequestDTO dto) {

        try {
            Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                    ? Sort.by(dto.getSortBy()).ascending()
                    : Sort.by(dto.getSortBy()).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<Locations> pageResult;

            if (dto.getLocationId() != null) {
                pageResult = repository.findByLocationId(
                        dto.getLocationId(),
                        pageable
                );
            } else {
                // locationId नसेल तर ALL locations
                pageResult = repository.findAll(pageable);
            }

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No locations found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<LocationResponseDTO> content = pageResult.getContent()
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


}
