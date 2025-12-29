package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitResponseDto;
import com.sipl.ticket.core.mapper.UnitMapper;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    /* ================= SAVE ================= */

    @Override
    public ApiResponseDTO<UnitResponseDto> saveUnit(UnitRequestDto dto) {

        log.info("<<Start>> saveUnit called <<Start>>");

        try {
            if (dto == null || dto.getUnitName() == null || dto.getUnitName().trim().isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit name is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Unit unit = unitMapper.toEntity(dto);
            unit.setIsActive(true);

            Unit saved = unitRepository.save(unit);

            return new ApiResponseDTO<>(
                    unitMapper.toResponseDto(saved),
                    "Unit created successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("saveUnit error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ================= UPDATE ================= */

    @Override
    public ApiResponseDTO<UnitResponseDto> updateUnit(UnitRequestDto dto) {

        log.info("<<Start>> updateUnit called <<Start>>");

        try {
            if (dto == null || dto.getUnitId() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit ID is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Unit unit = unitRepository.findById(dto.getUnitId())
                    .orElse(null);

            if (unit == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Unit not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            unitMapper.partialUpdate(dto, unit);

            Unit updated = unitRepository.save(unit);

            return new ApiResponseDTO<>(
                    unitMapper.toResponseDto(updated),
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

    /* ================= GET BY ID ================= */

    @Override
    public ApiResponseDTO<UnitResponseDto> getById(Long unitId) {

        log.info("<<Start>> getById called <<Start>>");

        try {
            return unitRepository.findById(unitId)
                    .map(unit -> new ApiResponseDTO<>(
                            unitMapper.toResponseDto(unit),
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
            log.error("getById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    /* ================= DELETE (SOFT) ================= */

    @Override
    public ApiResponseDTO<String> deleteById(Long unitId) {

        log.info("<<Start>> deleteById called <<Start>>");

        try {
            Unit unit = unitRepository.findById(unitId)
                    .orElse(null);

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
            unitRepository.save(unit);

            return new ApiResponseDTO<>(
                    null,
                    "Unit deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
