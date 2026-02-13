package com.sipl.ticket.slaRuleDetail.service.Impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dao.entity.SlaProfile;
import com.sipl.ticket.core.dao.entity.SlaRuleDetails;
import com.sipl.ticket.core.dao.repository.ServiceRepository;
import com.sipl.ticket.core.dao.repository.SlaProfileRepository;
import com.sipl.ticket.core.dao.repository.SlaRuleDetailsRepository;
import com.sipl.ticket.core.dto.request.SlaRuleDetailsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;
import com.sipl.ticket.core.helper.SlaRuleDetailsExcelGenerator;
import com.sipl.ticket.core.mapper.SlaRuleDetailsMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.slaRuleDetail.service.SlaRuleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SlaRuleDetailServiceImpl implements SlaRuleDetailService {

    private final SlaRuleDetailsRepository repository;
    private final SlaRuleDetailsMapper mapper;
    private final SlaProfileRepository profileRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @ActivityLoggable(
            action = "CREATE",
            module = "SLA_RULE_DETAILS",
            description = "SLA Rule created successfully"
    )
    public ApiResponseDTO<SlaRuleDetailsDto> save(SlaRuleDetailsDto dto) {

        try {
            log.info("Creating SLA Rule for profileId={}, serviceId={}, severityMasterId={}, slaTypeMasterId={}",
                    dto.getSlaProfile().getSlaProfileId(),
                    dto.getService().getServiceId(),
                    dto.getSeverityMasterId(),
                    dto.getSlaTypeMasterId()
            );

            if (repository.findActiveRule(
                    dto.getSlaProfile().getSlaProfileId(),
                    dto.getService().getServiceId(),
                    dto.getSeverityMasterId().intValue(),
                    dto.getSlaTypeMasterId()
            ).isPresent()) {

                log.warn("SLA Rule already exists for given combination");
                return new ApiResponseDTO<>(
                        null,
                        "SLA Rule already exists for given combination",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            SlaRuleDetails entity = mapper.toEntity(dto);

            ApiResponseDTO<SlaRuleDetailsDto> validation =
                    validateAndSetRelations(entity, dto);

            if (validation != null) return validation;

            entity.setIsActive(true);
            entity.setIsDeleted(false);

            SlaRuleDetails saved = repository.save(entity);

            log.info("SLA Rule created successfully with id={}", saved.getSlaRuleDetailId());

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "SLA Rule created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error while creating SLA Rule", e);
            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while creating SLA Rule",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ApiResponseDTO<SlaRuleDetailsDto> validateAndSetRelations(
            SlaRuleDetails entity,
            SlaRuleDetailsDto dto) {

        log.info("Validating SLA Profile and Service relations");

        SlaProfile profile =
                profileRepository.findById(dto.getSlaProfile().getSlaProfileId()).orElse(null);

        if (profile == null) {
            log.warn("Invalid SLA Profile id={}", dto.getSlaProfile().getSlaProfileId());
            return new ApiResponseDTO<>(
                    null,
                    "Invalid SLA Profile",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        ServiceEntity service =
                serviceRepository.findById(dto.getService().getServiceId()).orElse(null);

        if (service == null) {
            log.warn("Invalid Service id={}", dto.getService().getServiceId());
            return new ApiResponseDTO<>(
                    null,
                    "Invalid Service",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        entity.setSlaProfile(profile);
        entity.setService(service);

        log.info("Relations validated successfully");
        return null;
    }

    @Override
    @ActivityLoggable(
            action = "UPDATE",
            module = "SLA_RULE_DETAILS",
            description = "SLA Rule updated successfully"
    )
    public ApiResponseDTO<SlaRuleDetailsDto> update(SlaRuleDetailsDto dto) {

        log.info("Updating SLA Rule id={}", dto.getSlaRuleDetailId());

        SlaRuleDetails entity =
                repository.findById(dto.getSlaRuleDetailId()).orElse(null);

        if (entity == null || Boolean.TRUE.equals(entity.getIsDeleted())) {
            log.warn("SLA Rule not found or deleted id={}", dto.getSlaRuleDetailId());
            return new ApiResponseDTO<>(
                    null,
                    "SLA Rule not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        mapper.partialUpdate(dto, entity);

        if (dto.getSlaProfile() != null && dto.getService() != null) {
            ApiResponseDTO<SlaRuleDetailsDto> validation =
                    validateAndSetRelations(entity, dto);

            if (validation != null) return validation;
        }

        SlaRuleDetails saved = repository.save(entity);

        log.info("SLA Rule updated successfully id={}", saved.getSlaRuleDetailId());

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "SLA Rule updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<SlaRuleDetailsDto> getById(Integer id) {

        log.info("Fetching SLA Rule by id={}", id);

        return repository.findById(id)
                .filter(e -> !Boolean.TRUE.equals(e.getIsDeleted()))
                .map(e -> {
                    log.info("SLA Rule found id={}", id);
                    return new ApiResponseDTO<>(
                            mapper.toDto(e),
                            "SLA Rule found",
                            HttpStatus.OK,
                            false
                    );
                })
                .orElseGet(() -> {
                    log.warn("SLA Rule not found id={}", id);
                    return new ApiResponseDTO<>(
                            null,
                            "SLA Rule not found",
                            HttpStatus.NOT_FOUND,
                            true
                    );
                });
    }

    @Override
    @ActivityLoggable(
            action = "DELETE",
            module = "SLA_RULE_DETAILS",
            description = "SLA Rule deleted successfully"
    )
    public ApiResponseDTO<String> delete(Integer id) {

        log.info("Deleting SLA Rule id={}", id);

        SlaRuleDetails entity = repository.findById(id).orElse(null);

        if (entity == null) {
            log.warn("SLA Rule not found for delete id={}", id);
            return new ApiResponseDTO<>(
                    null,
                    "SLA Rule not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        entity.setIsActive(false);
        entity.setIsDeleted(true);

        repository.save(entity);

        log.info("SLA Rule soft deleted id={}", id);

        return new ApiResponseDTO<>(
                null,
                "SLA Rule deleted successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<PagedResponse<SlaRuleDetailsDto>> search(
            SlaRuleDetailsSearchRequestDto dto) {

        log.info("Searching SLA Rules page={}, size={}, slaProfileId={}, isActive={}",
                dto.getPage(),
                dto.getSize(),
                dto.getSlaProfileId(),
                dto.getIsActive());

        Pageable pageable = PaginationUtil.pageable(
                dto.getPage(),
                dto.getSize(),
                dto.getSortBy(),
                dto.getSortDir()
        );

        Page<SlaRuleDetails> pageResult =
                repository.findAllNotDeleted(
                        dto.getSlaProfileId(),
                        dto.getIsActive(),
                        pageable
                );

        List<SlaRuleDetailsDto> content =
                pageResult.getContent()
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList());

        if (content.isEmpty()) {
            log.warn("No SLA Rules found");
            return new ApiResponseDTO<>(
                    null,
                    "No SLA Rules found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        log.info("SLA Rules fetched count={}", content.size());

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "SLA Rules fetched successfully",
                HttpStatus.OK,
                false
        );
    }



    @Override
    public ApiResponseDTO<SlaRuleDetailsDto> getAll() {

        log.info("Fetching all SLA Rules");

        List<SlaRuleDetailsDto> list =
                repository.findAll(Sort.by(Sort.Direction.DESC, "slaRuleDetailId"))
                        .stream()
                        .filter(e -> !Boolean.TRUE.equals(e.getIsDeleted()))
                        .map(mapper::toDto)
                        .collect(Collectors.toList());

        if (list.isEmpty()) {
            log.warn("No SLA Rules found in getAll");
            return new ApiResponseDTO<>(
                    null,
                    "No SLA Rules found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        log.info("Total SLA Rules fetched={}", list.size());

        return new ApiResponseDTO<>(
                list,
                HttpStatus.OK,
                "SLA Rules fetched successfully",
                false,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportSlaRuleDetailsExcel(SlaRuleDetailsSearchRequestDto request) {

        log.info("Request | slaProfileId={} | isActive={}",
                request.getSlaProfileId(),
                request.getIsActive());

        try {

            Page<SlaRuleDetails> page =
                    repository.findAllNotDeleted(
                            request.getSlaProfileId(),
                            request.getIsActive(),
                            Pageable.unpaged()
                    );

            log.info("Total elements = {}", page.getTotalElements());

            List<SlaRuleDetails> list = page.getContent();

            if (list.isEmpty()) {
                log.warn("No SLA Rule Details found");
            }

            List<SlaRuleDetailsDto> dtos = new ArrayList<>();

            for (SlaRuleDetails entity : list) {
                dtos.add(mapper.toDto(entity));
            }

            log.info("Mapping completed | DTO count = {}", dtos.size());

            return SlaRuleDetailsExcelGenerator.generateExcel(dtos);

        } catch (Exception e) {
            log.error("Error while exporting SLA Rule Details", e);
            throw new RuntimeException("Failed to export SLA Rule Details Excel", e);
        }
    }


}
