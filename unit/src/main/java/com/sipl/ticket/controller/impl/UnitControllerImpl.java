package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.UnitController;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.service.UnitService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/units")
@CrossOrigin("*")
@Api(tags = "Unit APIs")
@RequiredArgsConstructor
@Slf4j
public class UnitControllerImpl implements UnitController {

    private final UnitService unitService;

    @Override
    @GetMapping("/{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable Long unitId) {
        log.info("REST request to fetch Unit {}", unitId);
        return ResponseEntity.ok(unitService.getUnitById(unitId));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UnitDto>> getAllUnits() {
        log.info("REST request to fetch all Units");
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    @Override
    @PostMapping
    public ResponseEntity<UnitDto> createUnit(
            @Valid @RequestBody UnitRequestDto requestDto
    ) {
        log.info("REST request to create Unit");
        return ResponseEntity.ok(unitService.createUnit(requestDto));
    }

    @Override
    @PutMapping("/{unitId}")
    public ResponseEntity<UnitDto> updateUnit(
            @PathVariable Long unitId,
            @Valid @RequestBody UnitRequestDto requestDto
    ) {
        log.info("REST request to update Unit {}", unitId);
        return ResponseEntity.ok(unitService.updateUnit(unitId, requestDto));
    }

    @Override
    @DeleteMapping("/{unitId}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long unitId) {
        log.info("REST request to delete Unit {}", unitId);
        unitService.deleteUnit(unitId);
        return ResponseEntity.noContent().build();
    }
}
