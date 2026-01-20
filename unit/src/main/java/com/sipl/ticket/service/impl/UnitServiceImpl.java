package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.request.UnitSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.UnitExcelGenerator;
import com.sipl.ticket.core.mapper.UnitMapper;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository repository;
    private final UnitMapper mapper;

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "UNIT",
            description = "Unit {0} created successfully"
    )
    public ApiResponseDTO<UnitDto> createUnit(UnitRequestDto dto) {
        try {
            String name = dto.getUnitName().trim();

            if (repository.existsByUnitNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Unit unit = new Unit();
            unit.setUnitName(name);
            unit.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
            unit.setIsDelete(false);

            Unit saved = repository.save(unit);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Unit created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("createUnit error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public ApiResponseDTO<UnitDto> updateUnit(Long unitId, UnitRequestDto dto) {

        if (dto == null || unitId == null) {
            throw new IllegalArgumentException("Unit ID is required");
        }

        Unit unit = repository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit"));

        EntityStateValidator.checkNotDeleted(
                unit.getIsDelete(),
                "Unit",
                unit.getUnitName()
        );

        boolean isUpdated = false;

        if (dto.getUnitName() != null && !dto.getUnitName().trim().isEmpty()) {
            unit.setUnitName(dto.getUnitName().trim());
            isUpdated = true;
        }

        if (dto.getIsActive() != null) {
            unit.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }

        Unit updated = repository.save(unit);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "Unit updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<UnitDto> getUnitById(Long unitId) {
        try {
            return repository.findById(unitId)
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .filter(u -> Boolean.FALSE.equals(u.getIsDelete()))
                    .map(u -> new ApiResponseDTO<>(
                            mapper.toDto(u),
                            "Unit found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Unit not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));
        } catch (Exception e) {
            log.error("getUnitById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "UNIT",
            description = "Unit id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteUnit(Long unitId) {
        try {
            Unit unit = repository.findById(unitId).orElse(null);

            if (unit == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.TRUE.equals(unit.getIsDelete())) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            unit.setIsActive(false);
            unit.setIsDelete(true);

            repository.save(unit);

            return new ApiResponseDTO<>(
                    null,
                    "Unit deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteUnit error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("units")
    public ApiResponseDTO<List<UnitDto>> getAllUnits() {
        try {
            List<UnitDto> list = repository.findAll(Sort.by(Sort.Direction.ASC, "unitName"))
                    .stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .filter(u -> Boolean.FALSE.equals(u.getIsDelete()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No units found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    "Units fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllUnits error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    // ================= EXPORT =================

    @Override
    public void exportUnitsExcel(HttpServletResponse response) {
        log.info("Exporting active units to Excel");

        try {
            List<UnitDto> units = repository.findAll()
                    .stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .filter(u -> Boolean.FALSE.equals(u.getIsDelete()))
                    .map(u -> {
                        UnitDto dto = new UnitDto();
                        dto.setUnitId(u.getUnitId());
                        dto.setUnitName(u.getUnitName());
                        dto.setIsActive(u.getIsActive());
                        return dto;
                    })
                    .collect(Collectors.toList());

            UnitExcelGenerator.generateExcel(units, response);

            log.info("Units Excel export completed, totalRecords={}", units.size());

        } catch (Exception e) {
            log.error("exportUnitsExcel error", e);
            throw new RuntimeException("Failed to export units Excel", e);
        }
    }

    // ================= SEARCH =================

    @Override
    public ApiResponseDTO<PagedResponse<UnitDto>> searchUnits(UnitSearchRequestDto dto) {
        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Unit> pageResult = repository.searchUnits(
                    dto.getQuery(),
                    dto.getIsActive(),
                    pageable
            );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No units found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<UnitDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<UnitDto> pagedResponse =
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
                    "Units fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchUnits error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
