package com.sipl.ticket.service.impl;

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

    @Override
    @CacheEvict(value = "cities", allEntries = true)
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

    @Override
    @CacheEvict(value = "cities", allEntries = true)
    public ApiResponseDTO<CityResponseDto> updateCity(CityRequestDto dto) {

        try {
            if (dto.getCityId() == null ||
                    dto.getCityName() == null ||
                    dto.getCityName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "City ID and name are required",
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

            City city = repository.findById(dto.getCityId()).orElse(null);

            if (city == null) {
                return new ApiResponseDTO<>(
                        null,
                        "City not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(city.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive city cannot be updated",
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
                        "Cannot assign city to inactive state",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getCityName().trim();

            if (repository.existsByCityNameIgnoreCaseAndState_StateIdAndCityIdNot(
                    name, dto.getStateId(), dto.getCityId())) {

                return new ApiResponseDTO<>(
                        null,
                        "City '" + name + "' already exists in this state",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            city.setCityName(name);
            city.setState(state);
            City updated = repository.save(city);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "City updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateCity error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<CityResponseDto> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
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

    @Override
    @CacheEvict(value = "cities", allEntries = true)
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

            if (Boolean.FALSE.equals(city.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "City already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            city.setIsActive(false);
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
    public ApiResponseDTO<PagedResponse<CityResponseDto>> searchCities(
            CitySearchRequestDto dto) {

        try {
            Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                    ? Sort.by(dto.getSortBy()).ascending()
                    : Sort.by(dto.getSortBy()).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<City> pageResult =
                    repository.searchCities(
                            dto.getCityId(),
                            dto.getStateId(),
                            dto.getCityName(),
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

            List<CityResponseDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<CityResponseDto> pagedResponse =
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

}