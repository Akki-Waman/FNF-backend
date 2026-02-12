package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dto.response.CitySearchResponseDto;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.CityExcelGenerator;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.CityService;
import com.sipl.ticket.core.dao.entity.City;
import com.sipl.ticket.core.dao.entity.State;
import com.sipl.ticket.core.dao.repository.CityRepository;
import com.sipl.ticket.core.dao.repository.StateRepository;
import com.sipl.ticket.core.dto.request.CityRequestDto;
import com.sipl.ticket.core.dto.request.CitySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CityResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.CityMapper;
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
public class CityServiceImpl implements CityService {

    private final CityRepository repository;
    private final StateRepository stateRepository;
    private final CityMapper mapper;

    @ActivityLoggable(
            action = "CREATE",
            module = "CITY",
            description = "City Id {0} created successfully"
    )
    public ApiResponseDTO<CityResponseDto> saveCity(CityRequestDto dto) {

        try {
            if (dto.getCityName() == null || dto.getCityName().trim().isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "City name is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            if (dto.getStateId() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "State ID is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            State state = stateRepository.findById(dto.getStateId()).orElse(null);

            if (state == null) {
                return new ApiResponseDTO<>(
                        null,
                        "State not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(state.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Cannot add city to inactive state",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getCityName().trim();

            if (repository.existsByCityNameIgnoreCaseAndState_StateId(
                    name, dto.getStateId())) {
                return new ApiResponseDTO<>(
                        null,
                        "City '" + name + "' already exists in this state",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            City city = new City();
            city.setCityName(name);
            city.setState(state);
            city.setIsActive(true);
            city.setIsDeleted(false);


            City saved = repository.save(city);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "City created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveCity error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

//    @ActivityLoggable(
//            action = "UPDATE",
//            module = "CITY",
//            description = "City {0} updated successfully"
//    )
//    public ApiResponseDTO<CityResponseDto> updateCity(CityRequestDto dto) {
//
//        try {
//            if (dto.getCityId() == null ||
//                    dto.getCityName() == null ||
//                    dto.getCityName().trim().isEmpty()) {
//
//                return new ApiResponseDTO<>(
//                        null,
//                        "City ID and name are required",
//                        HttpStatus.BAD_REQUEST,
//                        true
//                );
//            }
//
//            if (dto.getStateId() == null) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "State ID is required",
//                        HttpStatus.BAD_REQUEST,
//                        true
//                );
//            }
//
//            City city = repository.findById(dto.getCityId()).orElse(null);
//
//            if (city == null) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "City not found",
//                        HttpStatus.NOT_FOUND,
//                        true
//                );
//            }
//
//            if (Boolean.TRUE.equals(city.getIsDeleted())) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "City is deleted",
//                        HttpStatus.BAD_REQUEST,
//                        true
//                );
//            }
//
//            if (Boolean.FALSE.equals(city.getIsActive())) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "Inactive city cannot be updated",
//                        HttpStatus.BAD_REQUEST,
//                        true
//                );
//            }
//
//            State state = stateRepository.findById(dto.getStateId()).orElse(null);
//
//            if (state == null) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "State not found",
//                        HttpStatus.NOT_FOUND,
//                        true
//                );
//            }
//
//            if (Boolean.FALSE.equals(state.getIsActive())) {
//                return new ApiResponseDTO<>(
//                        null,
//                        "Cannot assign city to inactive state",
//                        HttpStatus.BAD_REQUEST,
//                        true
//                );
//            }
//
//            String name = dto.getCityName().trim();
//
//            if (repository.existsByCityNameIgnoreCaseAndState_StateIdAndCityIdNot(
//                    name, dto.getStateId(), dto.getCityId())) {
//
//                return new ApiResponseDTO<>(
//                        null,
//                        "City '" + name + "' already exists in this state",
//                        HttpStatus.CONFLICT,
//                        true
//                );
//            }
//
//            city.setCityName(name);
//            city.setState(state);
//            City updated = repository.save(city);
//
//            return new ApiResponseDTO<>(
//                    mapper.toDto(updated),
//                    "City updated successfully",
//                    HttpStatus.OK,
//                    false
//            );
//
//        } catch (Exception e) {
//            log.error("updateCity error", e);
//            return new ApiResponseDTO<>(
//                    null,
//                    "Internal server error",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    true
//            );
//        }
//    }

    @Override
    @ActivityLoggable(
            action = "UPDATE",
            module = "CITY",
            description = "City {0} updated successfully"
    )
    public ApiResponseDTO<CityResponseDto> updateCity(CityRequestDto dto) {

        if (dto == null || dto.getCityId() == null) {
            throw new IllegalArgumentException("City ID is required");
        }

        City city = repository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City"));

        EntityStateValidator.checkNotDeleted(
                city.getIsDeleted(),
                "City",
                city.getCityName()
        );

        boolean isUpdated = false;

        if (dto.getCityName() != null &&
                !dto.getCityName().trim().isEmpty()) {

            city.setCityName(dto.getCityName().trim());
            isUpdated = true;
        }

        if (dto.getStateId() != null) {

            State state = stateRepository.findById(dto.getStateId())
                    .orElseThrow(() -> new ResourceNotFoundException("State"));

            city.setState(state);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }
        city.setIsActive(dto.getIsActive());
        City updated = repository.save(city);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "City updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<CityResponseDto> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))

                    .map(c -> new ApiResponseDTO<>(
                            mapper.toDto(c),
                            "City found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "City not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getCityById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @ActivityLoggable(
            action = "DELETE",
            module = "CITY",
            description = "City {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long id) {

        try {
            City city = repository.findById(id).orElse(null);

            if (city == null) {
                return new ApiResponseDTO<>(
                        null,
                        "City not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.TRUE.equals(city.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "City already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            city.setIsDeleted(true);
            repository.save(city);

            return new ApiResponseDTO<>(
                    null,
                    "City deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteCity error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("cities")
    public ApiResponseDTO<CityResponseDto> getAllCities() {

        try {
            List<CityResponseDto> list = repository
                    .findAll(Sort.by(Sort.Direction.DESC, "cityId"))
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No cities found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Cities fetched successfully",
                    false, LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllCities error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable(value = "citiesByState", key = "#stateId")
    public ApiResponseDTO<CityResponseDto> getCitiesByStateId(Long stateId) {

        try {
            if (stateId == null) {
                return new ApiResponseDTO<>(
                        null,
                        "State ID is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            State state = stateRepository.findById(stateId).orElse(null);

            if (state == null) {
                return new ApiResponseDTO<>(
                        null,
                        "State not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<CityResponseDto> list = repository
                    .findByState_StateIdAndIsActiveTrue(stateId)
                    .stream()
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No cities found for this state",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Cities fetched successfully",
                    false, LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getCitiesByStateId error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<CitySearchResponseDto>> searchCities(
            CitySearchRequestDto dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<CitySearchResponseDto> pageResult =
                    repository.searchCities(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No cities found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            // ✅ DIRECT DTO (no mapper)
            PagedResponse<CitySearchResponseDto> pagedResponse =
                    new PagedResponse<>(
                            pageResult.getContent(),
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Cities fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchCities error", e);
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
    public void exportCitiesExcel(HttpServletResponse response) {

        log.info("Exporting active cities to Excel");

        try {

            List<CityResponseDto> cities =
                    repository.findAll()
                            .stream()
                            .filter(city -> Boolean.TRUE.equals(city.getIsActive()))
                            .filter(city -> Boolean.FALSE.equals(city.getIsDeleted()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            CityExcelGenerator.generateExcel(cities, response);

            log.info(
                    "Cities Excel export completed successfully | totalRecords={}",
                    cities.size()
            );

        } catch (Exception e) {
            log.error("exportCitiesExcel unexpected error", e);
            throw new RuntimeException("Failed to export cities Excel", e);
        }
    }
}