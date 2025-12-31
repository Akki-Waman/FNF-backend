package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.StateResponseDto;

public interface StateService {

    ApiResponseDTO<StateResponseDto> saveState(
            StateRequestDto stateRequestDto
    );

    ApiResponseDTO<StateResponseDto> updateState(
            StateRequestDto stateRequestDto
    );

    ApiResponseDTO<StateResponseDto> getById(
            Long stateId
    );

    ApiResponseDTO<String> deleteById(
            Long stateId
    );

    ApiResponseDTO<PagedResponse<StateResponseDto>> getAllStates();
}

