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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ApiResponseDTO<CountryResponseDto> createCountry(CountryRequestDto dto) {

        log.info("START :: createCountry | payload={}", dto);

        try {
            String name = dto.getCountryName().trim();

            if (repository.existsByCountryNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(null,
                        "Country already exists",
                        HttpStatus.CONFLICT,
                        true);
            }

            Country country = mapper.toEntity(dto);
            country.setCountryName(name);
            country.setIsActive(true);

            Country saved = repository.save(country);

            log.info("END :: createCountry | id={}", saved.getCountryId());

            return new ApiResponseDTO<>(mapper.toDto(saved),
                    "Country created successfully",
                    HttpStatus.CREATED,
                    false);

        } catch (Exception e) {
            log.error("ERROR :: createCountry", e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public ApiResponseDTO<CountryResponseDto> updateCountry(Long id, CountryRequestDto dto) {

        log.info("START :: updateCountry | id={}, payload={}", id, dto);

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

            log.info("END :: updateCountry | id={}", id);

            return new ApiResponseDTO<>(mapper.toDto(updated),
                    "Country updated successfully",
                    HttpStatus.OK,
                    false);

        } catch (Exception e) {
            log.error("ERROR :: updateCountry | id={}", id, e);
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

        log.info("START :: getCountryById | countryId={}", countryId);

        try {
            Country country = repository.findById(countryId).orElse(null);

            if (country == null || Boolean.FALSE.equals(country.getIsActive())) {
                log.info("END :: getCountryById | Country not found");
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            log.info("END :: getCountryById | Country found");
            return new ApiResponseDTO<>(
                    mapper.toDto(country),
                    "Country fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("ERROR :: getCountryById | countryId={}", countryId, e);
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
    public ApiResponseDTO<List<CountryResponseDto>> getAllCountries() {

        log.info("START :: getAllCountries");

        try {
            List<CountryResponseDto> countries =
                    repository.findAll()
                            .stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            if (countries.isEmpty()) {
                log.info("END :: getAllCountries | No records found");
                return new ApiResponseDTO<>(
                        null,
                        "No countries found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            log.info("END :: getAllCountries | count={}", countries.size());
            return new ApiResponseDTO<>(
                    countries,
                    "Countries fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("ERROR :: getAllCountries", e);
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

        log.info("START :: deleteCountry | countryId={}", countryId);

        try {
            if (countryId == null) {
                log.info("END :: deleteCountry | countryId is null");
                return new ApiResponseDTO<>(
                        null,
                        "Country id is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Country country = repository.findById(countryId).orElse(null);

            if (country == null) {
                log.info("END :: deleteCountry | Country not found");
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(country.getIsActive())) {
                log.info("END :: deleteCountry | Country already inactive");
                return new ApiResponseDTO<>(
                        null,
                        "Country already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            country.setIsActive(false);
            repository.save(country);

            log.info("END :: deleteCountry | Successfully deactivated");

            return new ApiResponseDTO<>(
                    null,
                    "Country deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("ERROR :: deleteCountry | countryId={}", countryId, e);
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
            value = "searchCountries",
            key = "T(java.util.Objects).hash(#name, #isForeign, #pageable.pageNumber, #pageable.pageSize)",
            unless = "#result == null || #result.error == true"
    )
    public ApiResponseDTO<Page<CountryResponseDto>> searchCountries(
            String name, Boolean isForeign, Pageable pageable) {

        log.info("START :: searchCountries | name={}, isForeign={}, page={}, size={}",
                name, isForeign, pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<Country> page = repository.searchCountries(
                    (name != null && !name.isBlank()) ? name.trim() : null,
                    isForeign,
                    pageable);

            if (page.isEmpty()) {
                return new ApiResponseDTO<>(null,
                        "No countries found",
                        HttpStatus.NOT_FOUND,
                        true);
            }

            Page<CountryResponseDto> dtoPage = page.map(mapper::toDto);

            log.info("END :: searchCountries | total={}", dtoPage.getTotalElements());

            return new ApiResponseDTO<>(dtoPage,
                    "Countries fetched successfully",
                    HttpStatus.OK,
                    false);

        } catch (Exception e) {
            log.error("ERROR :: searchCountries", e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }



}
