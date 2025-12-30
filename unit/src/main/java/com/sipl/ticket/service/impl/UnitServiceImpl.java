package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.repository.UnitRepository;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.core.mapper.UnitMapper;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    public UnitDto getUnitById(Long unitId) {
        log.info("Fetching active Unit with id {}", unitId);

        Unit unit = unitRepository.findActiveById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        return unitMapper.toDto(unit);
    }

    @Override
    public List<UnitDto> getAllUnits() {
        log.info("Fetching all active Units");
        return unitMapper.mapUnitListToDtoList(unitRepository.findAllActive());
    }

    @Override
    public UnitDto createUnit(UnitRequestDto requestDto) {
        log.info("Creating Unit with name {}", requestDto.getUnitName());

        Unit unit = new Unit();
        unit.setUnitName(requestDto.getUnitName());
        unit.setIsActive(requestDto.getIsActive());

        return unitMapper.toDto(unitRepository.save(unit));
    }

    @Override
    public UnitDto updateUnit(Long unitId, UnitRequestDto requestDto) {
        log.info("Updating Unit id {}", unitId);

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unit.setUnitName(requestDto.getUnitName());
        unit.setIsActive(requestDto.getIsActive());

        return unitMapper.toDto(unitRepository.save(unit));
    }

    @Override
    public void deleteUnit(Long unitId) {
        log.info("Soft deleting Unit id {}", unitId);

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unit.setIsActive(false);
        unitRepository.save(unit);
    }
}
