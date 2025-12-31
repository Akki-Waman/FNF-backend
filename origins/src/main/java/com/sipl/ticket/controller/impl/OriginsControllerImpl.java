package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.OriginsController;
import com.sipl.ticket.core.dto.request.OriginsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OriginDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.OriginsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OriginsControllerImpl implements OriginsController {

    private final OriginsService originsService;

    @Override
    public ResponseEntity<ApiResponseDTO<OriginDto>> save(
            @Valid @RequestBody OriginsRequestDto dto) {

        log.info("<<Start>> save Origin endpoint called <<Start>>");

        ApiResponseDTO<OriginDto> response =
                originsService.saveOrigin(dto);

        log.info("<<End>> save Origin endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<OriginDto>> update(
            @RequestBody OriginsRequestDto dto) {

        log.info("<<Start>> update Origin endpoint called <<Start>>");

        ApiResponseDTO<OriginDto> response =
                originsService.updateOrigin(dto);

        log.info("<<End>> update Origin endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<OriginDto>> getById(
            @PathVariable Long originId) {

        log.info("<<Start>> get Origin by id endpoint called <<Start>>");

        ApiResponseDTO<OriginDto> response =
                originsService.getById(originId);

        log.info("<<End>> get Origin by id endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long originId) {

        log.info("<<Start>> delete Origin endpoint called <<Start>>");

        ApiResponseDTO<String> response =
                originsService.deleteById(originId);

        log.info("<<End>> delete Origin endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<OriginDto>>> getAll() {

        log.info("<<Start>> getAll Origins endpoint called <<Start>>");

        ApiResponseDTO<PagedResponse<OriginDto>> response =
                originsService.getAllOrigins();

        log.info("<<End>> getAll Origins endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {

        log.info("<<Start>> download Origins CSV endpoint called <<Start>>");

        originsService.downloadExcel(response);

        log.info("<<End>> download Origins CSV endpoint called <<End>>");
    }
}

