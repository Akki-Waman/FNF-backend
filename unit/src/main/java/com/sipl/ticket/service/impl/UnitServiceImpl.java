package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.request.UnitSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.core.helper.UnitExcelGenerator;
import com.sipl.ticket.core.mapper.UnitMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.Cacheable;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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
    @ActivityLoggable(action = "CREATE", module = "UNIT")
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
    @CacheEvict(value = "units", allEntries = true)
    @ActivityLoggable(action = "UPDATE", module = "UNIT")
    public ApiResponseDTO<UnitDto> updateUnit(Long unitId, UnitRequestDto dto) {
        try {
            if (unitId == null || dto.getUnitName() == null || dto.getUnitName().trim().isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Unit unit = repository.findById(unitId).orElse(null);

            if (unit == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(unit.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive unit cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getUnitName().trim();

            if (repository.existsByUnitNameIgnoreCaseAndUnitIdNot(name, unitId)) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            unit.setUnitName(name);
            Unit updated = repository.save(unit);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Unit updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateUnit error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<UnitDto> getUnitById(Long unitId) {
        try {
            return repository.findById(unitId)
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
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
    @ActivityLoggable(action = "DELETE", module = "UNIT")
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

            if (Boolean.FALSE.equals(unit.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            unit.setIsActive(false);
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
            List<UnitDto> list = repository.findAll()
                    .stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
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
    @Override
    public void exportUnitsExcel(HttpServletResponse response) {

        log.info("Exporting active units to Excel");

        try {
            List<UnitDto> units = repository.findAll()
                    .stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .map(u -> {
                        UnitDto dto = new UnitDto();
                        dto.setUnitId(u.getUnitId());
                        dto.setUnitName(u.getUnitName());
                        dto.setIsActive(u.getIsActive());
                        return dto;
                    })
                    .collect(Collectors.toList()); // Java 8 safe

            UnitExcelGenerator.generateExcel(units, response);

            log.info(
                    "Units Excel export completed successfully, totalRecords={}",
                    units.size()
            );

        } catch (Exception e) {
            log.error("exportUnitsExcel unexpected error", e);
            throw new RuntimeException("Failed to export units Excel", e);
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<UnitDto>> searchUnits(
            UnitSearchRequestDto dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );


            Page<Unit> pageResult =
                    repository.searchUnits(
                            dto.getQuery(),
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


