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
    @ActivityLoggable(action = "CREATE", module = "SLA_PROFILE")
    public ApiResponseDTO<SlaProfileResponseDto> saveSlaProfile(
            SlaProfileRequestDto dto) {

        try {

            if (repository.existsByProfileNameIgnoreCaseAndBranch_BranchId(
                    dto.getProfileName(),
                    dto.getBranchId())) {

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

            if (validationError != null) {
                return validationError;
            }

            slaProfile.setIsActive(true);

            SlaProfile saved = repository.save(slaProfile);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "SLA Profile created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveSlaProfile error", e);
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
    @ActivityLoggable(action = "UPDATE", module = "SLA_PROFILE")
    public ApiResponseDTO<SlaProfileResponseDto> updateSlaProfile(
            SlaProfileRequestDto dto) {

        SlaProfile slaProfile =
                repository.findById(dto.getSlaProfileId()).orElse(null);

        if (slaProfile == null) {
            return new ApiResponseDTO<>(
                    null,
                    "SLA Profile not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (repository.existsByProfileNameIgnoreCaseAndBranch_BranchIdAndSlaProfileIdNot(
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

        if (validationError != null) {
            return validationError;
        }

        SlaProfile saved = repository.save(slaProfile);

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "SLA Profile updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<SlaProfileResponseDto> getById(
            Integer slaProfileId) {

        return repository.findById(slaProfileId)
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
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
    @ActivityLoggable(action = "DELETE", module = "SLA_PROFILE")
    public ApiResponseDTO<String> deleteById(
            Integer slaProfileId) {

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

        if (pageResult.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No SLA Profiles found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        List<SlaProfileResponseDto> content =
                pageResult.getContent()
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
                "SLA Profiles fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<SlaProfileResponseDto> getAllSlaProfiles() {

        try {
            List<SlaProfileResponseDto> list =
                    repository.findAll(Sort.by(Sort.Direction.DESC, "slaProfileId"))
                            .stream()
                            .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
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

        } catch (Exception e) {
            log.error("getAllSlaProfiles error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
