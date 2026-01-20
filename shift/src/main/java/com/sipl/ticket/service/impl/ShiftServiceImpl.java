package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Shift;
import com.sipl.ticket.core.dao.repository.ShiftRepository;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.ShiftExcelGenerator;
import com.sipl.ticket.core.mapper.ShiftMapper;
import com.sipl.ticket.core.util.EntityStateValidator;
import com.sipl.ticket.core.util.PaginationUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
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
    @ActivityLoggable(
            action = "CREATE",
            module = "SHIFT",
            description = "Shift {0} created successfully"
    )
    public ApiResponseDTO<ShiftResponseDTO> saveShift(ShiftRequestDto dto) {

        log.info("Saving shift with name: {}", dto.getShiftName());

        try {

            String shiftname = dto.getShiftName().trim();

            if (repository.existsByShiftNameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(shiftname)) {
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
            shift.setIsDeleted(false);
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
    @ActivityLoggable(
            action = "UPDATE",
            module = "SHIFT",
            description = "Shift {0} updated successfully"
    )
    public ApiResponseDTO<ShiftResponseDTO> updateShift(ShiftRequestDto dto) {

        log.info("Updating shift, id={}, name={}",
                dto != null ? dto.getShiftId() : null,
                dto != null ? dto.getShiftName() : null
        );

        if (dto == null || dto.getShiftId() == null) {
            throw new IllegalArgumentException("Shift ID is required");
        }

        Shift shift = repository.findById(dto.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift"));

        EntityStateValidator.checkNotDeleted(
                shift.getIsDeleted(),
                "Shift",
                shift.getShiftName()
        );

        boolean isUpdated = false;

        if (dto.getShiftName() != null && !dto.getShiftName().trim().isEmpty()) {

            String name = dto.getShiftName().trim();

            if (repository.existsByShiftNameAndIdNot(name,dto.getShiftId())) {
                throw new IllegalStateException(
                            "Shift '" + name + "' already exists"
                );
            }

            shift.setShiftName(name);
            isUpdated = true;
        }

        if (dto.getDescription() != null) {
            shift.setDescription(dto.getDescription());
            isUpdated = true;
        }

        if (dto.getStartTime() != null) {
            shift.setStartTime(dto.getStartTime());
            isUpdated = true;
        }

        if (dto.getEndTime() != null) {
            shift.setEndTime(dto.getEndTime());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No fields provided to update");
        }

        shift.setIsActive(dto.getIsActive());
        Shift updatedShift = repository.save(shift);

        return new ApiResponseDTO<>(
                mapper.toResponseDto(updatedShift),
                "Shift updated successfully",
                HttpStatus.OK,
                false
        );
    }


    @Override
    public ApiResponseDTO<ShiftResponseDTO> getById(Long id) {

        log.info("Fetching shift by id={}", id);

        try {
            return repository.findById(id)
                    .filter(s ->
                            Boolean.TRUE.equals(s.getIsActive())
                                    && Boolean.FALSE.equals(s.getIsDeleted())
                    )
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
    @ActivityLoggable(
            action = "DELETE",
            module = "SHIFT",
            description = "Shift id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deleting shift logically, id={}", id);

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


            if (Boolean.TRUE.equals(shift.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Shift is already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }


            shift.setIsDeleted(true);
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
    public ApiResponseDTO<ShiftResponseDTO> getAllShifts() {

        log.info("Fetching all active shifts");

        try {
            List<ShiftResponseDTO> list = repository.findAll()
                    .stream()
                    .filter(s ->
                            Boolean.TRUE.equals(s.getIsActive())
                                    && Boolean.FALSE.equals(s.getIsDeleted())
                    )
                    .map(mapper::toResponseDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No shifts found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Shifts fetched successfully",
                    false,
                    LocalDateTime.now()
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
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Shift> pageResult =
                    repository.findByShiftId(
                            dto.getQuery(),
                            dto.getIsActive(),
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
    @Override
    @Transactional(readOnly = true)
    public void downloadShiftsExcel(HttpServletResponse response) {

        log.info("<<START>> Download Shifts Excel service");

        try {

            List<ShiftResponseDTO> dtoList = repository.findAll()
                    .stream()
                    .filter(shift ->
                            Boolean.TRUE.equals(shift.getIsActive())
                                    && Boolean.FALSE.equals(shift.getIsDeleted())
                    )
                    .map(shift -> {

                        ShiftResponseDTO dto = new ShiftResponseDTO();

                        dto.setShiftId(shift.getShiftId());
                        dto.setShiftName(shift.getShiftName());

                        dto.setStartTime(shift.getStartTime());
                        dto.setEndTime(shift.getEndTime());
                        dto.setIsActive(shift.getIsActive());


                        dto.setCreatedBy(
                                shift.getCreatedBy() != null
                                        ? shift.getCreatedBy().getUserName()
                                        : null
                        );
                        dto.setCreatedTime(shift.getCreatedTime());

                        dto.setModifiedBy(
                                shift.getModifiedBy() != null
                                        ? shift.getModifiedBy().getUserName()
                                        : null
                        );
                        dto.setModifiedTime(shift.getModifiedTime());

                        return dto;
                    })
                    .collect(Collectors.toList());

            log.info("Total active shifts fetched for Excel download: {}",
                    dtoList.size());

            ShiftExcelGenerator.generateExcel(dtoList, response);

            log.info("Shifts Excel generated successfully");

        } catch (IOException ex) {

            log.error("IOException occurred while downloading Shifts Excel", ex);
            throw new RuntimeException("Failed to download Shifts Excel", ex);

        } catch (Exception ex) {

            log.error("Unexpected error occurred while downloading Shifts Excel", ex);
            throw new RuntimeException("Failed to download Shifts Excel", ex);
        }

        log.info("<<END>> Download Shifts Excel service");
    }

}
