package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Shift;
import com.sipl.ticket.core.dao.repository.ShiftRepository;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import com.sipl.ticket.core.mapper.ShiftMapper;
import com.sipl.ticket.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository repository;
    private final ShiftMapper mapper;

    @Override
    @CacheEvict(value = "shifts", allEntries = true)
    public ApiResponseDTO<ShiftResponseDTO> saveShift(ShiftRequestDto dto) {

        log.info("Saving shift with name: {}", dto.getShiftName());

        try {

            String shiftname = dto.getShiftName().trim();

            if (repository.existsByShiftNameIgnoreCaseAndIsActiveTrue(shiftname)) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift '" + shiftname + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Shift shift = new Shift();
            shift.setShiftName(shiftname);
            shift.setDescription(dto.getDescription());
            shift.setStartTime(dto.getStartTime());
            shift.setEndTime(dto.getEndTime());
            shift.setIsActive(true);

            Shift savedShift = repository.save(shift);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(savedShift),
                    "Shift created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving shift", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "shifts", allEntries = true)
    public ApiResponseDTO<ShiftResponseDTO> updateShift(ShiftRequestDto dto) {

        log.info("Updating shift, id={}, name={}", dto.getShiftId(), dto.getShiftName());

        try {

            Shift shift = repository.findById(dto.getShiftId()).orElse(null);

            if (shift == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(shift.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive shift cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getShiftName().trim();

            if (repository.existsByShiftNameIgnoreCaseAndIsActiveTrue(
                    name)) {

                return new ApiResponseDTO<>(
                        null,
                        "Shift with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            shift.setShiftName(name);
            shift.setDescription(dto.getDescription());
            shift.setStartTime(dto.getStartTime());
            shift.setEndTime(dto.getEndTime());

            Shift updatedShift = repository.save(shift);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(updatedShift),
                    "Shift updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateShift unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ShiftResponseDTO> getById(Long id) {

        log.info("Fetching shift by id={}", id);

        try {
            return repository.findById(id)
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .map(s -> new ApiResponseDTO<>(
                            mapper.toResponseDto(s),
                            "Shift found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Shift not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "shifts", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating shift, id={}", id);

        try {
            Shift shift = repository.findById(id).orElse(null);

            if (shift == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(shift.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            shift.setIsActive(false);
            repository.save(shift);

            return new ApiResponseDTO<>(
                    null,
                    "Shift deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("shifts")
    public ApiResponseDTO<PagedResponse<ShiftResponseDTO>> getAllShifts() {

        log.info("Fetching all active shifts");

        try {
            List<ShiftResponseDTO> response = repository.findAll()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .map(mapper::toResponseDto)
                    .collect(Collectors.toList());

            if (response.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No shifts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(response, 0, response.size(), 1, response.size(), true),
                    "Shifts fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllShifts unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public ApiResponseDTO<PagedResponse<ShiftResponseDTO>> searchShifts(
            ShiftSearchRequestDto dto) {

        try {
            Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                    ? Sort.by(dto.getSortBy()).ascending()
                    : Sort.by(dto.getSortBy()).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<Shift> pageResult =
                    repository.findByShiftId(
                            dto.getShiftId(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No shifts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ShiftResponseDTO> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toResponseDto)
                    .collect(Collectors.toList());

            PagedResponse<ShiftResponseDTO> pagedResponse =
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
                    "Shifts fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchShifts error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

}
