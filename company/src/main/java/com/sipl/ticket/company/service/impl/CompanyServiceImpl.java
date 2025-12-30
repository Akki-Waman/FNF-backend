package com.sipl.ticket.company.service.impl;

import com.sipl.ticket.company.service.CompanyService;
import com.sipl.ticket.core.dao.entity.Companies;
import com.sipl.ticket.core.dao.repository.CompanyRepository;
import com.sipl.ticket.core.dto.request.CompaniesRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.CompanyMapper;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public ApiResponseDTO<CompanyDto> saveCompany(CompaniesRequestDto dto) {

        try {
            String name = dto.getCompanyName().trim();

            if (repository.existsByCompanyNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Company '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Companies company = new Companies();
            company.setCompanyName(name);
            company.setIsActive(true);

            Companies saved = repository.save(company);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Company created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveCompany error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public ApiResponseDTO<CompanyDto> updateCompany(CompaniesRequestDto dto) {

        try {
            if (dto.getCompanyId() == null ||
                    dto.getCompanyName() == null ||
                    dto.getCompanyName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Company ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Companies company = repository.findById(dto.getCompanyId()).orElse(null);

            if (company == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Company not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(company.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive company cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getCompanyName().trim();

            if (repository.existsByCompanyNameIgnoreCaseAndCompanyIdNot(
                    name, dto.getCompanyId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Company '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            company.setCompanyName(name);
            Companies updated = repository.save(company);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Company updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateCompany error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<CompanyDto> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(c -> new ApiResponseDTO<>(
                            mapper.toDto(c),
                            "Company found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Company not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getCompanyById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        try {
            Companies company = repository.findById(id).orElse(null);

            if (company == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Company not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(company.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Company already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            company.setIsActive(false);
            repository.save(company);

            return new ApiResponseDTO<>(
                    null,
                    "Company deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteCompany error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("companies")
    public ApiResponseDTO<PagedResponse<CompanyDto>> getAllCompanies() {

        try {
            List<CompanyDto> list = repository.findAll()
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No companies found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            list, 0, list.size(), 1, list.size(), true
                    ),
                    "Companies fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllCompanies error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
