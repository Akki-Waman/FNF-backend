package com.sipl.ticket.slaRuleDetail.controller.impl;

import com.sipl.ticket.core.dto.request.SlaRuleDetailsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;
import com.sipl.ticket.slaRuleDetail.controller.SlaRuleDetailController;
import com.sipl.ticket.slaRuleDetail.service.SlaRuleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SlaRuleDetailControllerImpl implements SlaRuleDetailController {

    private final SlaRuleDetailService slaRuleDetailService;

    @Override
    public ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> save(
            SlaRuleDetailsDto dto) {

        log.info("<<Start>> saveSlaRuleDetail <<Start>>");

        ApiResponseDTO<SlaRuleDetailsDto> response =
                slaRuleDetailService.save(dto);

        log.info("<<End>> saveSlaRuleDetail <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> update(
            SlaRuleDetailsDto dto) {

        log.info("<<Start>> updateSlaRuleDetail <<Start>>");

        ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> response =
                ResponseEntity.ok(slaRuleDetailService.update(dto));

        log.info("<<End>> updateSlaRuleDetail <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> getById(
            Integer id) {

        log.info("<<Start>> getSlaRuleDetailById <<Start>>");

        ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> response =
                ResponseEntity.ok(slaRuleDetailService.getById(id));

        log.info("<<End>> getSlaRuleDetailById <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> delete(
            Integer id) {

        log.info("<<Start>> deleteSlaRuleDetail <<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(slaRuleDetailService.delete(id));

        log.info("<<End>> deleteSlaRuleDetail <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<SlaRuleDetailsDto>> getAll() {

        log.info("<<Start>> getAllSlaRuleDetails <<Start>>");

        ApiResponseDTO<SlaRuleDetailsDto> response =
                slaRuleDetailService.getAll();

        log.info("<<End>> getAllSlaRuleDetails <<End>>");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<SlaRuleDetailsDto>>> search(
            SlaRuleDetailsSearchRequestDto requestDto) {

        log.info("<<Start>> searchSlaRuleDetails <<Start>>");

        ApiResponseDTO<PagedResponse<SlaRuleDetailsDto>> response =
                slaRuleDetailService.search(requestDto);

        log.info("<<End>> searchSlaRuleDetails <<End>>");

        return ResponseEntity.ok(response);
    }
}
