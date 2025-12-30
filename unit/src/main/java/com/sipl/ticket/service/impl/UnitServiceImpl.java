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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    public ApiResponseDTO<UnitDto> saveUnit(UnitRequestDto dto) {
        log.info("saveUnit called");

        Unit unit = unitMapper.toEntity(dto);
        unit.setIsActive(true);

        Unit saved = unitRepository.save(unit);

        return new ApiResponseDTO<>(
                unitMapper.toDto(saved),
                "Unit created successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<UnitDto> updateUnit(UnitRequestDto dto) {
        log.info("updateUnit called");

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unitMapper.partialUpdate(dto, unit);

        return new ApiResponseDTO<>(
                unitMapper.toDto(unitRepository.save(unit)),
                "Unit updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<UnitDto> getById(Long unitId) {
        return unitRepository.findById(unitId)
                .map(unit -> new ApiResponseDTO<>(
                        unitMapper.toDto(unit),
                        "Unit found",
                        HttpStatus.OK,
                        false
                ))
                .orElse(new ApiResponseDTO<>(
                        null,
                        "Unit not found",
                        HttpStatus.NOT_FOUND,
                        true
                ));
    }

    @Override
    public ApiResponseDTO<String> deleteById(Long unitId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unit.setIsActive(false);
        unitRepository.save(unit);

        return new ApiResponseDTO<>(
                null,
                "Unit deleted successfully",
                HttpStatus.OK,
                false
        );
    }
}
