package com.sipl.ticket.branch.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.branch.service.BranchService;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.BranchRequestDto;
import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.BranchExcelGenerator;
import com.sipl.ticket.core.mapper.BranchMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;


    @ActivityLoggable(
            action = "CREATE",
            module = "BRANCH",
            description = "Branch {0} created successfully"
    )
    public ApiResponseDTO<BranchDto> saveBranch(BranchRequestDto dto) {
        try {

            if (repository.existsByEmailIgnoreCase(dto.getEmail())) {
                return new ApiResponseDTO<>(
                        null,
                        "Branch with email already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            Branches branch = mapper.toEntity(dto);

            ApiResponseDTO<BranchDto> validationError =
                    validateAndSetRelations(branch, dto);

            if (validationError != null) {
                return validationError;
            }

            branch.setIsActive(true);
            branch.setIsClient(false);

            Branches saved = repository.save(branch);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Branch created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while creating branch",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ApiResponseDTO<BranchDto> validateAndSetRelations(
            Branches branch,
            BranchRequestDto dto) {

        Country country = countryRepository.findById(dto.getCountryId()).orElse(null);
        if (country == null || Boolean.FALSE.equals(country.getIsActive())) {
            return new ApiResponseDTO<>(
                    null, "Invalid country", HttpStatus.BAD_REQUEST, true
            );
        }

        State state = stateRepository.findById(dto.getStateId()).orElse(null);
        if (state == null || Boolean.FALSE.equals(state.getIsActive())) {
            return new ApiResponseDTO<>(
                    null, "Invalid state", HttpStatus.BAD_REQUEST, true
            );
        }

        City city = cityRepository.findById(dto.getCityId()).orElse(null);
        if (city == null || Boolean.FALSE.equals(city.getIsActive())) {
            return new ApiResponseDTO<>(
                    null, "Invalid city", HttpStatus.BAD_REQUEST, true
            );
        }

        Companies company = companyRepository.findById(dto.getCompanyId()).orElse(null);
        if (company == null) {
            return new ApiResponseDTO<>(
                    null, "Invalid company", HttpStatus.BAD_REQUEST, true
            );
        }

        branch.setCountry(country);
        branch.setState(state);
        branch.setCity(city);
        branch.setCompany(company);

        return null;
    }

    @ActivityLoggable(
            action = "UPDATE",
            module = "BRANCH",
            description = "Branch {0} updated successfully"
    )
    public ApiResponseDTO<BranchDto> updateBranch(BranchRequestDto dto) {

        if (dto == null || dto.getBranchId() == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch ID is required",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        Branches branch = repository.findById(dto.getBranchId()).orElse(null);

        if (branch == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (dto.getEmail() != null &&
                repository.existsByEmailIgnoreCaseAndBranchIdNot(
                        dto.getEmail(), dto.getBranchId())) {

            return new ApiResponseDTO<>(
                    null,
                    "Email already exists",
                    HttpStatus.CONFLICT,
                    true
            );
        }

        boolean isUpdated = false;

        if (dto.getEmail() != null ||
                dto.getBranchName() != null ||
                dto.getAddress() != null) {

            mapper.partialUpdate(dto, branch);
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            branch.setIsActive(dto.getIsActive());
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

        Branches saved = repository.save(branch);

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "Branch updated successfully",
                HttpStatus.OK,
                false
        );
    }



    @Override
    public ApiResponseDTO<BranchDto> getById(Integer branchId) {

        return repository.findById(branchId)
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .map(b -> new ApiResponseDTO<>(
                        mapper.toDto(b),
                        "Branch found",
                        HttpStatus.OK,
                        false
                ))
                .orElseGet(() -> new ApiResponseDTO<>(
                        null,
                        "Branch not found",
                        HttpStatus.NOT_FOUND,
                        true
                ));
    }

    @ActivityLoggable(
            action = "DELETE",
            module = "BRANCH",
            description = "Branch Id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Integer branchId) {

        Branches branch = repository.findById(branchId).orElse(null);

        if (branch == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        branch.setIsActive(false);
        repository.save(branch);

        return new ApiResponseDTO<>(
                null,
                "Branch deleted successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<PagedResponse<BranchDto>> searchBranches(
            BranchSearchRequestDto dto) {

        Pageable pageable = PaginationUtil.pageable(
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir()
        );
        Page<Branches> pageResult = repository.searchBranches(
                dto.getQuery(),
                dto.getIsActive(),
                pageable
        );

        if (pageResult.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No branches found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        List<BranchDto> content = pageResult.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "Branches fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<BranchDto> getAllBranches() {

        try {
            List<BranchDto> list = repository
                    .findAll(Sort.by(Sort.Direction.DESC, "branchId"))
                    .stream()
                    .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No branches found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Branches fetched successfully",
                    false,
                    null
            );

        } catch (Exception e) {
            log.error("getAllBranches error", e);
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
        public void exportBranchesCsv(HttpServletResponse response) {

            log.info("Exporting active branches to Excel");

            try {
                List<BranchDto> branches = repository.findAll()
                        .stream()
                        .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                        .map(mapper::toDto)
                        .collect(Collectors.toList());

                BranchExcelGenerator.generateExcel(branches, response);

                log.info("Branches Excel export completed successfully, totalRecords={}",
                        branches.size());

            } catch (Exception e) {
                log.error("exportBranchesCsv unexpected error", e);
                throw new RuntimeException("Failed to export branches Excel", e);
            }
        }



}
