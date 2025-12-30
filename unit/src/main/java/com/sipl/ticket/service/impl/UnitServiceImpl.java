package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.core.mapper.UnitMapper;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    /* ================= SAVE ================= */

    @Override
    public ApiResponseDTO<UnitDto> saveUnit(UnitRequestDto unitDto) {

        log.info("<<Start>> saveUnit <<Start>>");

        Unit unit = unitMapper.toEntity(unitDto);
        unit.setIsActive(true);

        Unit saved = unitRepository.save(unit);

        log.info("<<End>> saveUnit <<End>>");

        return new ApiResponseDTO<>(
                unitMapper.toDto(saved),
                "Unit created successfully",
                HttpStatus.OK,
                false
        );
    }

    /* ================= UPDATE ================= */

    @Override
    public ApiResponseDTO<UnitDto> updateUnit(UnitRequestDto dto) {

        log.info("<<Start>> updateUnit <<Start>>");

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unitMapper.partialUpdate(unitDto, unit);

        Unit updated = unitRepository.save(unit);

        log.info("<<End>> updateUnit <<End>>");

        return new ApiResponseDTO<>(
                unitMapper.toResponseDto(updated),
                "Unit updated successfully",
                HttpStatus.OK,
                false
        );
    }

    /* ================= GET BY ID ================= */

    @Override
    public ApiResponseDTO<UnitDto> getById(Long unitId) {

        log.info("<<Start>> getById <<Start>>");

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        log.info("<<End>> getById <<End>>");

        return new ApiResponseDTO<>(
                unitMapper.toResponseDto(unit),
                "Unit found",
                HttpStatus.OK,
                false
        );
    }

    /* ================= GET ALL ================= */

    @Override
    public ApiResponseDTO<List<UnitDto>> getAllUnits() {

        log.info("<<Start>> getAllUnits <<Start>>");

        List<UnitDto> units = unitRepository.findByIsActiveTrue()
                .stream()
                .map(unitMapper::toResponseDto)
                .collect(Collectors.toList());

        log.info("<<End>> getAllUnits <<End>>");

        return new ApiResponseDTO<>(
                units,
                "Units fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    /* ================= DELETE (SOFT) ================= */

    @Override
    public ApiResponseDTO<String> deleteById(Long unitId) {

        log.info("<<Start>> deleteById <<Start>>");

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unit.setIsActive(false);
        unitRepository.save(unit);

        log.info("<<End>> deleteById <<End>>");

        return new ApiResponseDTO<>(
                null,
                "Unit deleted successfully",
                HttpStatus.OK,
                false
        );
    }
}
