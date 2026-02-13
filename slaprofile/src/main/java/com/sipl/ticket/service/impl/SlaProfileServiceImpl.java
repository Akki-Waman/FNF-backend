package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dao.entity.SlaProfile;
import com.sipl.ticket.core.dao.repository.BranchRepository;
import com.sipl.ticket.core.dao.repository.SlaProfileRepository;
import com.sipl.ticket.core.dto.request.SlaProfileRequestDto;
import com.sipl.ticket.core.dto.request.SlaProfileSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import com.sipl.ticket.core.helper.SlaProfileExcelGenerator;
import com.sipl.ticket.core.mapper.SlaProfileMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.SlaProfileService;
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
public class SlaProfileServiceImpl implements SlaProfileService {

    private final SlaProfileRepository repository;
    private final SlaProfileMapper mapper;
    private final BranchRepository branchRepository;

    @Override
    @ActivityLoggable(
            action = "CREATE",
            module = "SLA_PROFILE",
            description = "SLA profile {0} created successfully"
    )
    public ApiResponseDTO<SlaProfileResponseDto> saveSlaProfile(SlaProfileRequestDto dto) {

        try {

            if (repository.existsActiveProfileForBranch(
                    dto.getProfileName(),
                    dto.getBranchId())) {

                log.warn("Duplicate SLA Profile detected | branchId={}, profileName={}",
                        dto.getBranchId(), dto.getProfileName());

                return new ApiResponseDTO<>(
                        null,
                        "SLA Profile already exists for this branch",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            SlaProfile slaProfile = mapper.toEntity(dto);

            ApiResponseDTO<SlaProfileResponseDto> validationError =
                    validateAndSetRelations(slaProfile, dto);

            if (validationError != null) return validationError;

            slaProfile.setIsActive(true);
            slaProfile.setIsDeleted(false);

            SlaProfile saved = repository.save(slaProfile);

            log.info("SLA Profile created | id={}", saved.getSlaProfileId());

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "SLA Profile created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error while creating SLA Profile", e);
            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while creating SLA Profile",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ApiResponseDTO<SlaProfileResponseDto> validateAndSetRelations(
            SlaProfile slaProfile,
            SlaProfileRequestDto dto) {

        Branches branch = branchRepository.findById(dto.getBranchId()).orElse(null);

        if (branch == null || Boolean.FALSE.equals(branch.getIsActive())) {
            return new ApiResponseDTO<>(
                    null,
                    "Invalid branch",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        slaProfile.setBranch(branch);
        return null;
    }

    @Override
    @ActivityLoggable(
            action = "UPDATE",
            module = "SLA_PROFILE",
            description = "SLA profile {0} updated successfully"
    )
    public ApiResponseDTO<SlaProfileResponseDto> updateSlaProfile(SlaProfileRequestDto dto) {

        SlaProfile slaProfile =
                repository.findById(dto.getSlaProfileId()).orElse(null);

        if (slaProfile == null || Boolean.TRUE.equals(slaProfile.getIsDeleted())) {
            return new ApiResponseDTO<>(
                    null,
                    "SLA Profile not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (repository.existsActiveProfileForBranchAndNotId(
                dto.getProfileName(),
                dto.getBranchId(),
                dto.getSlaProfileId())) {

            return new ApiResponseDTO<>(
                    null,
                    "SLA Profile already exists for this branch",
                    HttpStatus.CONFLICT,
                    true
            );
        }

        mapper.partialUpdate(dto, slaProfile);

        ApiResponseDTO<SlaProfileResponseDto> validationError =
                validateAndSetRelations(slaProfile, dto);

        if (validationError != null) return validationError;

        SlaProfile saved = repository.save(slaProfile);

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "SLA Profile updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<SlaProfileResponseDto> getById(Integer slaProfileId) {

        return repository.findById(slaProfileId)
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(p -> new ApiResponseDTO<>(
                        mapper.toDto(p),
                        "SLA Profile found",
                        HttpStatus.OK,
                        false
                ))
                .orElseGet(() -> new ApiResponseDTO<>(
                        null,
                        "SLA Profile not found",
                        HttpStatus.NOT_FOUND,
                        true
                ));
    }

    @Override
    @ActivityLoggable(
            action = "DELETE",
            module = "SLA_PROFILE",
            description = "SLA profile id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Integer slaProfileId) {

        SlaProfile slaProfile =
                repository.findById(slaProfileId).orElse(null);

        if (slaProfile == null) {
            return new ApiResponseDTO<>(
                    null,
                    "SLA Profile not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        slaProfile.setIsActive(false);
        slaProfile.setIsDeleted(true);

        repository.save(slaProfile);

        return new ApiResponseDTO<>(
                null,
                "SLA Profile deleted successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<PagedResponse<SlaProfileResponseDto>> searchSlaProfiles(
            SlaProfileSearchRequestDto dto) {

        Pageable pageable = PaginationUtil.pageable(
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir()
        );

        Page<SlaProfile> pageResult = repository.searchSlaProfiles(
                dto.getSlaProfileId(),
                dto.getBranchId(),
                dto.getIsActive(),
                pageable
        );

        List<SlaProfileResponseDto> content =
                pageResult.getContent()
                        .stream()
                        .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                        .map(mapper::toDto)
                        .collect(Collectors.toList());

        if (content.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No SLA Profiles found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "SLA Profiles fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<SlaProfileResponseDto> getAllSlaProfiles() {

        List<SlaProfileResponseDto> list =
                repository.findAll(Sort.by(Sort.Direction.DESC, "slaProfileId"))
                        .stream()
                        .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                        .map(mapper::toDto)
                        .collect(Collectors.toList());

        if (list.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No SLA Profiles found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        return new ApiResponseDTO<>(
                list,
                HttpStatus.OK,
                "SLA Profiles fetched successfully",
                false,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void exportSlaProfilesExcel(HttpServletResponse response) {

        log.info("Exporting SLA profiles to Excel");

        try {
            List<SlaProfileResponseDto> profiles =
                    repository.findAll()
                            .stream()
                            .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            SlaProfileExcelGenerator.generateExcel(profiles, response);

            log.info("SLA Profile Excel export completed successfully, totalRecords={}", profiles.size());

        } catch (Exception e) {
            log.error("exportSlaProfilesExcel unexpected error", e);
            throw new RuntimeException("Failed to export SLA profiles Excel", e);
        }
    }

}
