package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.entity.State;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dao.repository.StateRepository;
import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.request.StateSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.StateResponseDto;
import com.sipl.ticket.core.mapper.StateMapper;
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

    @Override
    @CacheEvict(value = "states", allEntries = true)
    public ApiResponseDTO<StateResponseDto> saveState(StateRequestDto dto) {
        log.info("Request received to save State");
        try {
            String stateName = dto.getStateName().trim();
            Long countryId = dto.getCountryId();
            log.debug("Saving state with name='{}', countryId={}", stateName, countryId);

            Country country = countryRepository.findById(countryId).orElse(null);
            if (country == null) {
                log.warn("Country not found for id={}", countryId);
                return new ApiResponseDTO<>(null, "Country not found", HttpStatus.NOT_FOUND, true);
            }

            boolean exists = stateRepository.existsByStateNameIgnoreCase(stateName);
            if (exists) {
                log.warn("State already exists with name='{}'", stateName);
                return new ApiResponseDTO<>(null, "State '" + stateName + "' already exists", HttpStatus.CONFLICT, true);
            }

            State state = new State();
            state.setStateName(stateName);
            state.setCountry(country);
            state.setIsActive(true);

            State saved = stateRepository.save(state);
            log.info("State saved successfully, id={}", saved.getStateId());

            return new ApiResponseDTO<>(mapper.toDto(saved), "State created successfully", HttpStatus.CREATED, false);

        } catch (Exception e) {
            log.error("Error occurred while saving state", e);
            return new ApiResponseDTO<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    @CacheEvict(value = "states", allEntries = true)
    public ApiResponseDTO<StateResponseDto> updateState(StateRequestDto dto) {
        log.info("Request received to update State, id={}", dto.getStateId());
        try {
            State state = stateRepository.findById(dto.getStateId()).orElse(null);
            if (state == null) {
                log.warn("State not found, id={}", dto.getStateId());
                return new ApiResponseDTO<>(null, "State not found", HttpStatus.NOT_FOUND, true);
            }

            if (Boolean.FALSE.equals(state.getIsActive())) {
                log.warn("Inactive state cannot be updated, id={}", dto.getStateId());
                return new ApiResponseDTO<>(null, "Inactive state cannot be updated", HttpStatus.BAD_REQUEST, true);
            }

            String stateName = dto.getStateName().trim();
            boolean exists = stateRepository.existsByStateNameIgnoreCaseAndStateIdNot(stateName, dto.getStateId());
            if (exists) {
                log.warn("Duplicate state name found='{}'", stateName);
                return new ApiResponseDTO<>(null, "State '" + stateName + "' already exists", HttpStatus.CONFLICT, true);
            }

            state.setStateName(stateName);
            State updated = stateRepository.save(state);
            log.info("State updated successfully, id={}", updated.getStateId());

            return new ApiResponseDTO<>(mapper.toDto(updated), "State updated successfully", HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Error occurred while updating state", e);
            return new ApiResponseDTO<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    public ApiResponseDTO<StateResponseDto> getById(Long stateId) {
        log.info("Fetching state by id={}", stateId);
        try {
            return stateRepository.findById(stateId)
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .map(s -> new ApiResponseDTO<>(mapper.toDto(s), "State found", HttpStatus.OK, false))
                    .orElseGet(() -> new ApiResponseDTO<>(null, "State not found", HttpStatus.NOT_FOUND, true));

        } catch (Exception e) {
            log.error("Error occurred while fetching state, id={}", stateId, e);
            return new ApiResponseDTO<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    @CacheEvict(value = "states", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long stateId) {
        log.info("Request received to delete state, id={}", stateId);
        try {
            State state = stateRepository.findById(stateId).orElse(null);
            if (state == null) {
                return new ApiResponseDTO<>(null, "State not found", HttpStatus.NOT_FOUND, true);
            }

            if (Boolean.FALSE.equals(state.getIsActive())) {
                return new ApiResponseDTO<>(null, "State already inactive", HttpStatus.BAD_REQUEST, true);
            }

            state.setIsActive(false);
            stateRepository.save(state);
            log.info("State deactivated successfully, id={}", stateId);

            return new ApiResponseDTO<>(null, "State deleted successfully", HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Error occurred while deleting state, id={}", stateId, e);
            return new ApiResponseDTO<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    @Cacheable("states")
    public ApiResponseDTO<List<StateResponseDto>> getAllStates() {
        log.info("Fetching all active states");
        try {
            List<StateResponseDto> list = stateRepository.findAll()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(null, "No states found", HttpStatus.NOT_FOUND, true);
            }

            return new ApiResponseDTO<>(list, "States fetched successfully", HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Error occurred while fetching states", e);
            return new ApiResponseDTO<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    // 🔥 NEW SEARCH METHOD ADD
    @Override
    public ApiResponseDTO<PagedResponse<StateResponseDto>> searchStates(StateSearchRequestDto dto) {
        log.info("<<Start>> search State service called <<Start>>");
        try {
            // Sorting: Default ascending by stateName
            Sort sort = Sort.by("stateName").ascending();

            Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

            Page<State> pageResult = stateRepository.searchStates(dto.getStateId(), pageable);

            if (pageResult.isEmpty()) {
                log.info("<<End>> search State service called <<End>>");
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

            PagedResponse<StateResponseDto> pagedResponse = new PagedResponse<>(
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

}
