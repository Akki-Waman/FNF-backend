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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    public ApiResponseDTO<UnitDto> getUnitById(Long unitId) {

        try {
            return unitRepository.findById(unitId)
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .map(u -> new ApiResponseDTO<>(
                            unitMapper.toDto(u),
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
    public ApiResponseDTO<List<UnitDto>> getAllUnits() {

        try {
            List<UnitDto> list = unitRepository.findAll()
                    .stream()
                    .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                    .map(unitMapper::toDto)
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
    public ApiResponseDTO<UnitDto> createUnit(UnitRequestDto requestDto) {

        try {

            Unit unit = new Unit();
            unit.setUnitName(requestDto.getUnitName().trim());
            unit.setIsActive(
                    requestDto.getIsActive() != null
                            ? requestDto.getIsActive()
                            : true
            );

            Unit saved = unitRepository.save(unit);

            return new ApiResponseDTO<>(
                    unitMapper.toDto(saved),
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
    public ApiResponseDTO<UnitDto> updateUnit(Long unitId, UnitRequestDto requestDto) {

        try {
            Unit unit = unitRepository.findById(unitId).orElse(null);

            if (Boolean.FALSE.equals(unit.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive unit cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            if (requestDto.getUnitName() != null
                    && !requestDto.getUnitName().trim().isEmpty()) {
                unit.setUnitName(requestDto.getUnitName().trim());
            }

            if (requestDto.getIsActive() != null) {
                unit.setIsActive(requestDto.getIsActive());
            }

            Unit updated = unitRepository.save(unit);

            return new ApiResponseDTO<>(
                    unitMapper.toDto(updated),
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
    public ApiResponseDTO<String> deleteUnit(Long unitId) {

        try {
            Unit unit = unitRepository.findById(unitId).orElse(null);

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
            log.error("deleteUnit error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
