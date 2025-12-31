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
    public ApiResponseDTO<ShiftResponseDTO> saveShift(ShiftRequestDto dto) {

        log.info("Saving shift with name: {}", dto.getShiftName());

        try {
            if (dto == null || dto.getShiftName() == null || dto.getShiftName().trim().isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift name is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getShiftName().trim();

            // ✅ DUPLICATE CHECK (same as Service)
            if (repository.existsByShiftNameIgnoreCaseAndIsActiveTrue(name)) {
                log.warn("Shift already exists with name: {}", name);
                return new ApiResponseDTO<>(
                        null,
                        "Shift '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Shift shift = mapper.toEntity(dto);
            shift.setShiftName(name);
            shift.setIsActive(true);

            Shift saved = repository.save(shift);

            log.info("Shift created successfully with id: {}", saved.getShiftId());

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(saved),
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
    public ApiResponseDTO<ShiftResponseDTO> updateShift(ShiftRequestDto dto) {

        log.info("Updating shift id={}", dto.getShiftId());

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

            mapper.partialUpdate(dto, shift);
            Shift updated = repository.save(shift);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(updated),
                    "Shift updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error while updating shift", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ShiftResponseDTO> getById(Long shiftId) {

        log.info("Fetching shift id={}", shiftId);

        try {
            return repository.findById(shiftId)
                    .filter(Shift::getIsActive)
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
            log.error("Error while fetching shift", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<String> deleteById(Long shiftId) {

        log.info("Deleting shift id={}", shiftId);

        try {
            Shift shift = repository.findById(shiftId).orElse(null);

            if (shift == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift not found",
                        HttpStatus.NOT_FOUND,
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
            log.error("Error while deleting shift", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<ShiftResponseDTO>> getAllShifts() {

        log.info("Fetching all shifts");

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
                    new PagedResponse<>(response, 0, response.size(), 1, response.size(), true),
                    "Shifts fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error while fetching shifts", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
