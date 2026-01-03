package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.request.CountrySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.core.mapper.CountryMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public ApiResponseDTO<CountryResponseDto> createCountry(CountryRequestDto dto) {

        log.info("Creating country={}", dto);

        try {
            String name = dto.getCountryName().trim();

            if (repository.existsByCountryNameIgnoreCase(name)) {
                log.warn("Country creation failed, duplicate name={}", name);
                return new ApiResponseDTO<>(null,
                        "Country already exists",
                        HttpStatus.CONFLICT,
                        true);
            }

            Country country = mapper.toEntity(dto);
            country.setCountryName(name);
            country.setIsActive(true);

            Country saved = repository.save(country);

            log.info("Country created successfully, id={}, name={}",
                    saved.getCountryId(), saved.getCountryName());

            return new ApiResponseDTO<>(mapper.toDto(saved),
                    "Country created successfully",
                    HttpStatus.CREATED,
                    false);

        } catch (Exception e) {
            log.error("createCountry unexpected error, payload={}", dto, e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public ApiResponseDTO<CountryResponseDto> updateCountry(Long id, CountryRequestDto dto) {

        log.info("Updating country, id={}, payload={}", id, dto);

        try {
            Country country = repository.findById(id).orElse(null);

            if (country == null) {
                return new ApiResponseDTO<>(null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true);
            }

            if (dto.getCountryName() != null) {
                String name = dto.getCountryName().trim();
                if (repository.existsByCountryNameIgnoreCaseAndCountryIdNot(name, id)) {
                    log.warn("Update failed, duplicate country name={}, id={}", name, id);
                    return new ApiResponseDTO<>(null,
                            "Duplicate country name",
                            HttpStatus.CONFLICT,
                            true);
                }
                country.setCountryName(name);
            }

            if (dto.getIsActive() != null) country.setIsActive(dto.getIsActive());
            if (dto.getIsForeign() != null) country.setIsForeign(dto.getIsForeign());
            if (dto.getTaxType() != null) country.setTaxType(dto.getTaxType().trim());

            Country updated = repository.save(country);

            log.info("Country updated successfully, id={}", updated.getCountryId());

            return new ApiResponseDTO<>(mapper.toDto(updated),
                    "Country updated successfully",
                    HttpStatus.OK,
                    false);

        } catch (Exception e) {
            log.error("updateCountry unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    @Override
    @Cacheable(
            value = "countryById",
            key = "#countryId",
            unless = "#result == null || #result.error == true"
    )
    public ApiResponseDTO<CountryResponseDto> getCountryById(Long countryId) {

        log.info("Fetching country by id={}", countryId);

        try {
            Country country = repository.findById(countryId).orElse(null);

            if (country == null || Boolean.FALSE.equals(country.getIsActive())) {
                log.warn("Country not found or inactive, id={}", countryId);
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            log.info("Country found, id={}, name={}",
                    country.getCountryId(), country.getCountryName());

            return new ApiResponseDTO<>(
                    mapper.toDto(country),
                    "Country fetched successfully",
                    HttpStatus.OK,
                    false
            );

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
    @Cacheable(
            value = "allCountries",
            unless = "#result == null || #result.error == true"
    )
    public ApiResponseDTO<CountryResponseDto> getAllCountries() {

        log.info("Fetching all active countries");

        try {
            List<CountryResponseDto> countries =
                    repository.findAll()
                            .stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            if (countries.isEmpty()) {
                log.warn("No active countries found");
                return new ApiResponseDTO<>(
                        null,
                        "No countries found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            log.info("Active countries fetched successfully, count={}", countries.size());
            return new ApiResponseDTO<>(
                    countries,
                    HttpStatus.OK,
                    "Countries fetched successfully",
                    false,
                    LocalDateTime.now()
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
    public ApiResponseDTO<String> deleteCountry(Long countryId) {

        log.info("Deleting country, id={}", countryId);

        try {
            if (countryId == null) {
                log.warn("deleteCountry failed, countryId is null");
                return new ApiResponseDTO<>(
                        null,
                        "Country id is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Country country = repository.findById(countryId).orElse(null);

            if (country == null) {
                log.warn("deleteCountry failed, country not found, id={}", countryId);
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(country.getIsActive())) {
                log.warn("Country already inactive, id={}", countryId);
                return new ApiResponseDTO<>(
                        null,
                        "Country already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            country.setIsActive(false);
            repository.save(country);

            log.info("Country deactivated successfully, id={}", countryId);

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




    @Override
    //@Cacheable(value = "countrySearchCache", key = "#requestDto")
    public ResponseEntity<ApiResponseDTO<Page<CountryResponseDto>>> searchCountries(
            CountrySearchRequestDto requestDto) {

        log.info("Searching countries with filters={}", requestDto);

        try {
            Pageable pageable = PaginationUtil.pageable(
                    requestDto.getPage(),
                    requestDto.getSize(),
                    requestDto.getSortBy(),
                    requestDto.getSortDir()
            );

            Page<Country> countryPage = repository.searchCountries(
                    requestDto.getCountryId(),
                    requestDto.getCountryName(),
                    requestDto.getTaxType(),
                    requestDto.getIsForeign(),
                    requestDto.getIsActive(),
                    pageable
            );

            if (countryPage.isEmpty()) {
                log.warn("searchCountries returned no results for filters={}", requestDto);
                return ResponseEntity.ok(
                        new ApiResponseDTO<>(
                                "No countries found",
                                HttpStatus.NOT_FOUND,
                                false
                        )
                );
            }

            Page<CountryResponseDto> responsePage =
                    countryPage.map(mapper::toDto);

            log.info("Countries search successful, totalElements={}",
                    responsePage.getTotalElements());

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            responsePage,
                            "Countries fetched successfully",
                            HttpStatus.OK,
                            false
                    )
            );

        } catch (Exception e) {
            log.error("searchCountries unexpected error, request={}", requestDto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(
                            "Internal server error",
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            true
                    ));
        }
    }




}
