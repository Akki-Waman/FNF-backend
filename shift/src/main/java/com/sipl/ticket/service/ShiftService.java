package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ShiftService {

    ApiResponseDTO<ShiftResponseDTO> saveShift(
            ShiftRequestDto shiftRequestDto
    );

    ApiResponseDTO<ShiftResponseDTO> updateShift(
            ShiftRequestDto shiftRequestDto
    );

    ApiResponseDTO<ShiftResponseDTO> getById(
            Long shiftId
    );

    ApiResponseDTO<String> deleteById(
            Long shiftId
    );

    ApiResponseDTO<ShiftResponseDTO> getAllShifts();

    ApiResponseDTO<PagedResponse<ShiftResponseDTO>> searchShifts(
            ShiftSearchRequestDto requestDto
    );

}
