package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.OriginsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OriginDto;
import com.sipl.ticket.core.dto.response.PagedResponse;

import javax.servlet.http.HttpServletResponse;

public interface OriginsService {

    ApiResponseDTO<OriginDto> saveOrigin(
            OriginsRequestDto originsRequestDto
    );

    ApiResponseDTO<OriginDto> updateOrigin(
            OriginsRequestDto originsRequestDto
    );

    ApiResponseDTO<OriginDto> getById(
            Long originId
    );

    ApiResponseDTO<String> deleteById(
            Long originId
    );

    ApiResponseDTO<PagedResponse<OriginDto>> getAllOrigins();


    void downloadExcel(HttpServletResponse response);
}

