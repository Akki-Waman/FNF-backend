package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.request.CountrySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.core.helper.CountryExcelGenerator;
import com.sipl.ticket.core.mapper.CountryMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;
    private final CountryMapper mapper;

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "COUNTRY",
            description = "Country {0} created successfully"
    )
    public ApiResponseDTO<CountryResponseDto> createCountry(CountryRequestDto dto) {

        log.info("Creating country={}", dto);

        try {
            String name = dto.getCountryName().trim();

            if (repository.countActiveCountry(name) > 0) {
                return new ApiResponseDTO<>(
                        null,
                        "Country already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }


            Country country = mapper.toEntity(dto);
            country.setCountryName(name);
            country.setIsActive(true);
            country.setIsDeleted(false);

            Country saved = repository.save(country);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Country created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("createCountry error", e);
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
    @ActivityLoggable(
            action = "UPDATE",
            module = "COUNTRY",
            description = "Country {0} updated successfully"
    )
    public ApiResponseDTO<CountryResponseDto> updateCountry(
            Long id,
            CountryRequestDto dto
    ) {

        if (id == null || dto == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Country ID is required",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        Country country = repository.findById(id).orElse(null);

        if (country == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Country not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (Boolean.TRUE.equals(country.getIsDeleted())) {
            return new ApiResponseDTO<>(
                    null,
                    "Country is deleted",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        boolean isUpdated = false;

        if (dto.getCountryName() != null &&
                !dto.getCountryName().trim().isEmpty()) {

            String name = dto.getCountryName().trim();

            if (repository.existsByCountryNameIgnoreCaseAndCountryIdNot(name, id)) {
                return new ApiResponseDTO<>(
                        null,
                        "Country '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            country.setCountryName(name);
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            country.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (dto.getIsForeign() != null) {
            country.setIsForeign(dto.getIsForeign());
            isUpdated = true;
        }

        if (dto.getTaxType() != null &&
                !dto.getTaxType().trim().isEmpty()) {

            country.setTaxType(dto.getTaxType().trim());
            isUpdated = true;
        }

        if (!isUpdated) {
            return new ApiResponseDTO<>(
                    null,
                    "No fields provided to update",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        Country updated = repository.save(country);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "Country updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Cacheable(
            value = "countryById",
            key = "#countryId",
            unless = "#result == null || #result.error == true"
    )
    public ApiResponseDTO<CountryResponseDto> getCountryById(Long countryId) {

        try {
            Country country = repository.findById(countryId).orElse(null);

            if (country == null
                    || Boolean.FALSE.equals(country.getIsActive())
                    || Boolean.TRUE.equals(country.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Country not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    mapper.toDto(country),
                    "Country fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getCountryById error", e);
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

        try {
            List<CountryResponseDto> countries =
                    repository.findAll(Sort.by(Sort.Direction.ASC, "countryName"))
                            .stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                            .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
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
                    HttpStatus.OK,
                    "Countries fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllCountries error", e);
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
    @ActivityLoggable(
            action = "DELETE",
            module = "COUNTRY",
            description = "Country id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteCountry(Long countryId) {

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

            if (Boolean.TRUE.equals(country.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Country already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            country.setIsActive(false);
            country.setIsDeleted(true);
            repository.save(country);

            return new ApiResponseDTO<>(
                    null,
                    "Country deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteCountry error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Page<CountryResponseDto>>> searchCountries(
            CountrySearchRequestDto requestDto) {

        Pageable pageable = PaginationUtil.pageable(
                requestDto.getPage(),
                requestDto.getSize(),
                requestDto.getSortBy(),
                requestDto.getSortDir()
        );

        Page<Country> page = repository.searchCountries(
                requestDto.getQuery(),
                requestDto.getIsActive(),
                pageable
        );

        Page<CountryResponseDto> responsePage = page.map(mapper::toDto);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        responsePage,
                        "Countries fetched successfully",
                        HttpStatus.OK,
                        false
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void exportCountriesExcel(HttpServletResponse response) {

        try {
            List<CountryResponseDto> countries =
                    repository.findAll()
                            .stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                            .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            CountryExcelGenerator.generateExcel(countries, response);

        } catch (Exception e) {
            log.error("exportCountriesExcel error", e);
            throw new RuntimeException("Failed to export countries Excel", e);
        }
    }
}
