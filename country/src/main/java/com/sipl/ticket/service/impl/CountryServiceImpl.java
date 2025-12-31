package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.core.mapper.CountryMapper;
import com.sipl.ticket.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;
    private final CountryMapper mapper;

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public ApiResponseDTO<CountryResponseDto> createCountry(
            CountryRequestDto dto) {

        log.info("Saving country with name: {}", dto.getCountryName());

        try {
            String name = dto.getCountryName().trim();

            if (repository.existsByCountryNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Country '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Country country = new Country();
            country.setCountryName(name);
            country.setIsActive(true);

            Country savedCountry = repository.save(country);

            return new ApiResponseDTO<>(
                    mapper.toDto(savedCountry),
                    "Country created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving country", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public ApiResponseDTO<CountryResponseDto> updateCountry(
            Long countryId,
            CountryRequestDto dto) {

        log.info("Updating country, id={}, name={}",
                countryId, dto.getCountryName());

        try {
            if (dto == null || dto.getCountryName() == null ||
                    dto.getCountryName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Country name is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Country country = repository.findById(countryId).orElse(null);

            if (country == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(country.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive country cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getCountryName().trim();

            if (repository.existsByCountryNameIgnoreCaseAndCountryIdNot(
                    name, countryId)) {

                return new ApiResponseDTO<>(
                        null,
                        "Country '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            country.setCountryName(name);
            Country updatedCountry = repository.save(country);

            return new ApiResponseDTO<>(
                    mapper.toDto(updatedCountry),
                    "Country updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateCountry unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<CountryResponseDto> getCountryById(
            Long countryId) {

        log.info("Fetching country by id={}", countryId);

        try {
            return repository.findById(countryId)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(c -> new ApiResponseDTO<>(
                            mapper.toDto(c),
                            "Country found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Country not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getCountryById unexpected error, id={}", countryId, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("countries")
    public ApiResponseDTO<List<CountryResponseDto>> getAllCountries() {

        log.info("Fetching all active countries");

        try {
            List<CountryResponseDto> countries = repository.findAll()
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (countries.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No countries found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    countries,
                    "Countries fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllCountries unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public ApiResponseDTO<String> deleteCountry(
            Long countryId) {

        log.info("Deactivating country, id={}", countryId);

        try {
            Country country = repository.findById(countryId).orElse(null);

            if (country == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(country.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Country is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            country.setIsActive(false);
            repository.save(country);

            return new ApiResponseDTO<>(
                    null,
                    "Country deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteCountry unexpected error, id={}", countryId, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
