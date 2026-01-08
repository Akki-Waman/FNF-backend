package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.SlaProfileController;
import com.sipl.ticket.core.dto.request.SlaProfileRequestDto;
import com.sipl.ticket.core.dto.request.SlaProfileSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import com.sipl.ticket.service.SlaProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SlaProfileControllerImpl implements SlaProfileController {

    private final SlaProfileService slaProfileService;

    @Override
    public ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> saveSlaProfile(
            @Valid @RequestBody SlaProfileRequestDto dto) {

        log.info("<<Start>> saveSlaProfile  <<Start>>");

        ApiResponseDTO<SlaProfileResponseDto> response =
                slaProfileService.saveSlaProfile(dto);

        log.info("<<End>> saveSlaProfile <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> updateSlaProfile(
            SlaProfileRequestDto dto) {

        log.info("<<Start>> updateSlaProfile  <<Start>>");

        ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> response =
                ResponseEntity.ok(slaProfileService.updateSlaProfile(dto));

        log.info("<<End>> updateSlaProfile <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> getById(
            Integer slaProfileId) {

        log.info("<<Start>> getById  <<Start>>");

        ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> response =
                ResponseEntity.ok(slaProfileService.getById(slaProfileId));

        log.info("<<End>> getById  <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Integer slaProfileId) {

        log.info("<<Start>> deleteById  <<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(slaProfileService.deleteById(slaProfileId));

        log.info("<<End>> deleteById  <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> getAllSlaProfiles() {

        log.info("<<Start>> getAllSlaProfiles <<Start>>");

        ApiResponseDTO<SlaProfileResponseDto> response =
                slaProfileService.getAllSlaProfiles();

        log.info("<<End>> getAllSlaProfiles <<End>>");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<SlaProfileResponseDto>>> searchSlaProfiles(
            SlaProfileSearchRequestDto requestDto) {

        log.info("<<Start>> searchSlaProfiles <<Start>>");

        ApiResponseDTO<PagedResponse<SlaProfileResponseDto>> response =
                slaProfileService.searchSlaProfiles(requestDto);

        log.info("<<End>> searchSlaProfiles <<End>>");

        return ResponseEntity.ok(response);
    }
}
