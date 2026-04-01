package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.SlaProfileRequestDto;
import com.sipl.ticket.core.dto.request.SlaProfileSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface SlaProfileService {

    ApiResponseDTO<SlaProfileResponseDto> saveSlaProfile(
            SlaProfileRequestDto slaProfileRequestDto
    );

    ApiResponseDTO<SlaProfileResponseDto> updateSlaProfile(
            SlaProfileRequestDto slaProfileRequestDto
    );

    ApiResponseDTO<SlaProfileResponseDto> getById(
            Integer slaProfileId
    );

    ApiResponseDTO<String> deleteById(
            Integer slaProfileId
    );

    ApiResponseDTO<SlaProfileResponseDto> getAllSlaProfiles();

    ApiResponseDTO<PagedResponse<SlaProfileResponseDto>> searchSlaProfiles(
            SlaProfileSearchRequestDto requestDto
    );

    byte[] exportSlaProfilesExcel(SlaProfileSearchRequestDto request);

}
