package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.StateController;
import com.sipl.ticket.service.StateService;
import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.StateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StateControllerImpl implements StateController {

    private final StateService stateService;

    @Override
    public ResponseEntity<ApiResponseDTO<StateResponseDto>> save(
            @Valid @RequestBody StateRequestDto dto) {

        log.info("<<Start>> save State endpoint called <<Start>>");

        ApiResponseDTO<StateResponseDto> response =
                stateService.saveState(dto);

        log.info("<<End>> save State endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<StateResponseDto>> update(
            @RequestBody StateRequestDto dto) {

        log.info("<<Start>> update State endpoint called <<Start>>");

        ApiResponseDTO<StateResponseDto> response =
                stateService.updateState(dto);

        log.info("<<End>> update State endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<StateResponseDto>> getById(
            @PathVariable Long stateId) {

        log.info("<<Start>> get State by id endpoint called <<Start>>");

        ApiResponseDTO<StateResponseDto> response =
                stateService.getById(stateId);

        log.info("<<End>> get State by id endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long stateId) {

        log.info("<<Start>> delete State endpoint called <<Start>>");

        ApiResponseDTO<String> response =
                stateService.deleteById(stateId);

        log.info("<<End>> delete State endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<StateResponseDto>>> getAll() {

        log.info("<<Start>> get-all States endpoint called <<Start>>");

        ApiResponseDTO<PagedResponse<StateResponseDto>> response =
                stateService.getAllStates();

        log.info("<<End>> get-all States endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
