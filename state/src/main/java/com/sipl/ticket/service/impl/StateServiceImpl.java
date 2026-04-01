package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.entity.State;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dao.repository.StateRepository;
import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.request.StateSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.StateResponseDto;
import com.sipl.ticket.core.helper.StateExcelGenerator;
import com.sipl.ticket.core.mapper.StateMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.StateService;
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
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final StateMapper mapper;

    // ================= CREATE =================
    @Override
    @CacheEvict(value = "states", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "STATE",
            description = "State {0} created successfully"
    )
    public ApiResponseDTO<StateResponseDto> saveState(StateRequestDto dto) {

        log.info("Request received to save State");

        try {
            String stateName = dto.getStateName().trim();
            Long countryId = dto.getCountryId();

            Country country = countryRepository.findById(countryId).orElse(null);
            if (country == null) {
                return new ApiResponseDTO<>(null, "Country not found", HttpStatus.NOT_FOUND, true);
            }

            boolean exists = stateRepository.existsByStateNameAndCountry(stateName, countryId);
            if (exists) {
                return new ApiResponseDTO<>(null,
                        "State '" + stateName + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            State state = new State();
            state.setStateName(stateName);
            state.setCountry(country);
            state.setIsActive(true);
            state.setIsDeleted(false);

            State saved = stateRepository.save(state);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "State created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving state", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "states", allEntries = true)
    @ActivityLoggable(
            action = "UPDATE",
            module = "STATE",
            description = "State {0} updated successfully"
    )
    public ApiResponseDTO<StateResponseDto> updateState(StateRequestDto dto) {

        if (dto == null || dto.getStateId() == null) {
            return new ApiResponseDTO<>(
                    null,
                    "State ID is required",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        State state = stateRepository.findById(dto.getStateId()).orElse(null);

        if (state == null) {
            return new ApiResponseDTO<>(
                    null,
                    "State not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (Boolean.TRUE.equals(state.getIsDeleted())) {
            return new ApiResponseDTO<>(
                    null,
                    "State is deleted",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        boolean isUpdated = false;

        // ===== update state name =====
        if (dto.getStateName() != null && !dto.getStateName().trim().isEmpty()) {


            String name = dto.getStateName().trim();

            if (stateRepository.existsByStateNameIgnoreCaseAndStateIdNot(
                    name, dto.getStateId())) {

                return new ApiResponseDTO<>(
                        null,
                        "State '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            state.setStateName(name);
            isUpdated = true;
        }

        // ===== activate / deactivate =====
        if (dto.getIsActive() != null) {
            state.setIsActive(dto.getIsActive());
            isUpdated = true;
        }

        if (!isUpdated) {
            return new ApiResponseDTO<>(
                    null,
                    "No fields provided to update",
                    HttpStatus.BAD_REQUEST,
                    true
            );
        }

        State updated = stateRepository.save(state);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "State updated successfully",
                HttpStatus.OK,
                false
        );
    }

    // ================= GET BY ID =================
    @Override
    public ApiResponseDTO<StateResponseDto> getById(Long stateId) {

        log.info("Fetching state by id={}", stateId);

        try {
            return stateRepository.findById(stateId)
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .filter(s -> Boolean.FALSE.equals(s.getIsDeleted()))
                    .map(s -> new ApiResponseDTO<>(
                            mapper.toDto(s),
                            "State found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "State not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("Error occurred while fetching state", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    // ================= DELETE (SOFT) =================
    @Override
    @CacheEvict(value = "states", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "STATE",
            description = "State id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long stateId) {

        log.info("Request received to delete state, id={}", stateId);

        try {
            State state = stateRepository.findById(stateId).orElse(null);

            if (state == null) {
                return new ApiResponseDTO<>(
                        null,
                        "State not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }


            if (Boolean.TRUE.equals(state.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "State already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            state.setIsActive(false);
            state.setIsDeleted(true);
            stateRepository.save(state);

            return new ApiResponseDTO<>(
                    null,
                    "State deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while deleting state", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    // ================= GET ALL =================
    @Override
    @Cacheable("states")
    public ApiResponseDTO<StateResponseDto> getAllStates() {

        log.info("Fetching all active states");

        try {
            List<StateResponseDto> list = stateRepository.findAll()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .filter(s -> Boolean.FALSE.equals(s.getIsDeleted()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No states found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "States fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("Error occurred while fetching states", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    // ================= SEARCH =================
    @Override
    public ApiResponseDTO<PagedResponse<StateResponseDto>> searchStates(
            StateSearchRequestDto dto
    ) {

        log.info("<<Start>> search State service called <<Start>>");

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<State> pageResult =
                    stateRepository.searchStates(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No states found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<StateResponseDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<StateResponseDto> pagedResponse =
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
                    "States fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchStates error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    // ================= EXCEL =================
    @Override
    @Transactional(readOnly = true)
    public void downloadExcel(HttpServletResponse response) {

        log.info("<<START>> Download States Excel service");

        try {
            List<StateResponseDto> dtoList = stateRepository.findAll()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .filter(s -> Boolean.FALSE.equals(s.getIsDeleted()))
                    .map(state -> {
                        StateResponseDto dto = new StateResponseDto();
                        dto.setStateId(state.getStateId());
                        dto.setStateName(state.getStateName());
                        dto.setIsActive(state.getIsActive());
                        dto.setCountryName(
                                state.getCountry() != null
                                        ? state.getCountry().getCountryName()
                                        : null
                        );
                        dto.setCreatedBy(
                                state.getCreatedBy() != null
                                        ? state.getCreatedBy().getUserName()
                                        : null
                        );
                        dto.setCreatedTime(state.getCreatedTime());
                        dto.setModifiedBy(
                                state.getModifiedBy() != null
                                        ? state.getModifiedBy().getUserName()
                                        : null
                        );
                        dto.setModifiedTime(state.getModifiedTime());
                        return dto;
                    })
                    .collect(Collectors.toList());

            StateExcelGenerator.generateExcel(dtoList, response);

        } catch (Exception e) {
            log.error("Error while downloading States Excel", e);
            throw new RuntimeException("Failed to download States Excel", e);
        }

        log.info("<<END>> Download States Excel service");
    }
}
