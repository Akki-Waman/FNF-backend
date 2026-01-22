package com.sipl.ticket.company.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.company.service.CompanyService;
import com.sipl.ticket.core.dao.entity.Companies;
import com.sipl.ticket.core.dao.repository.CompanyRepository;
import com.sipl.ticket.core.dto.request.CompaniesRequestDto;
import com.sipl.ticket.core.dto.request.CompanySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.CompanyExcelGenerator;
import com.sipl.ticket.core.mapper.CompanyMapper;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.core.util.PaginationUtil;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "COMPANY",
            description = "Company {0} created successfully"
    )
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
            company.setIsDeleted(false);


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
    @ActivityLoggable(
            action = "UPDATE",
            module = "COMPANY",
            description = "Company {0} updated successfully"
    )
    public ApiResponseDTO<CompanyDto> updateCompany(CompaniesRequestDto dto) {

        if (dto == null || dto.getCompanyId() == null) {
            throw new IllegalArgumentException("Company ID is required");
        }

        Companies company = repository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company"));

        EntityStateValidator.checkNotDeleted(
                company.getIsDeleted(),
                "Company",
                company.getCompanyName()
        );

        boolean isUpdated = false;

        if (dto.getCompanyName() != null &&
                !dto.getCompanyName().trim().isEmpty()) {

            company.setCompanyName(dto.getCompanyName().trim());
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            company.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }

        Companies updated = repository.save(company);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "Company updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<CompanyDto> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
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
    @ActivityLoggable(
            action = "DELETE",
            module = "COMPANY",
            description = "Company id {0} deleted successfully"
    )
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

            if (Boolean.TRUE.equals(company.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Company already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }


            company.setIsActive(false);
            company.setIsDeleted(true);
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
    public ApiResponseDTO<CompanyDto> getAllCompanies() {

        try {
            List<CompanyDto> list = repository
                    .findAll(Sort.by(Sort.Direction.ASC, "companyName"))
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
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
                    list,
                    HttpStatus.OK,
                    "Companies fetched successfully",
                    false, LocalDateTime.now()
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

    @Override
    public ApiResponseDTO<PagedResponse<CompanyDto>> searchCompanies(
            CompanySearchRequestDto dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()

                    );
            Page<Companies> pageResult =
                    repository.searchCompanies(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No companies found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<CompanyDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<CompanyDto> pagedResponse =
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
                    "Companies fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchCompanies error", e);
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
    public void exportCompanies(HttpServletResponse response) {

        log.info("<<Start>> exportCompaniesExcel <<Start>>");

        try {
            List<CompanyDto> companies = repository.findAll(
                            Sort.by(Sort.Direction.DESC, "companyId"))
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .filter(c -> Boolean.FALSE.equals(c.getIsDeleted()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            CompanyExcelGenerator.generateExcel(companies, response);

            log.info("<<Success>> exportCompaniesExcel <<Success>>");

        } catch (Exception e) {
            log.error("<<Error>> exportCompaniesExcel failed", e);
            throw new RuntimeException("Failed to export companies excel", e);
        }

        log.info("<<End>> exportCompaniesExcel <<End>>");
    }

}
