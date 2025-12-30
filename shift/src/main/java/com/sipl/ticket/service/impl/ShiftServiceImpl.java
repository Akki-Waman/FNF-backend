package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Shift;
import com.sipl.ticket.core.dao.repository.ShiftRepository;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import com.sipl.ticket.core.mapper.ShiftMapper;
import com.sipl.ticket.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        log.info("<<Start>>saveShift endpoint called<<Start>>");

        try {
            if (dto == null ||
                    dto.getShiftName() == null || dto.getShiftName().trim().isEmpty() ||
                    dto.getStartTime() == null ||
                    dto.getEndTime() == null) {

                return new ApiResponseDTO<>(
                        null,
                        "Shift name, start time and end time are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String shiftName = dto.getShiftName().trim();

            Shift shift = mapper.toEntity(dto);
            shift.setShiftName(shiftName);
            shift.setIsActive(true);

            Shift saved = repository.save(shift);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(saved),
                    "Shift " + shiftName + " created successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("saveShift error", e);
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

        log.info("<<Start>>updateShift endpoint called<<Start>>");

        try {
            if (dto == null || dto.getShiftId() == null ||
                    dto.getShiftName() == null || dto.getShiftName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Shift ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Shift shift = repository.findById(dto.getShiftId()).orElse(null);

            if (shift == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            mapper.partialUpdate(dto, shift);
            shift.setShiftName(dto.getShiftName().trim());

            Shift updated = repository.save(shift);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(updated),
                    "Shift " + updated.getShiftName() + " updated successfully",
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

        log.info("<<Start>>getById endpoint called<<Start>>");

        try {
            return repository.findById(id)
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
            log.error("getById unexpected error", e);
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

        log.info("<<Start>>deleteById endpoint called<<Start>>");

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
                        "Shift already deactivated",
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
            log.error("Error while deleting shift, id={}", id, e);
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

        log.info("<<Start>>getAllShifts endpoint called<<Start>>");

        try {
            List<Shift> list = repository.findAll();

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No shifts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ShiftResponseDTO> response = mapper.toResponseDtoList(list);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            response,
                            0,
                            response.size(),
                            1,
                            response.size(),
                            true
                    ),
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
}
