package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ReminderCreateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;

public interface ReminderService {

    ApiResponseDTO<ReminderResponseDto> createReminder(ReminderCreateRequestDto request);

    ApiResponseDTO<ReminderResponseDto> updateReminder(
            Long reminderId,
            ReminderCreateRequestDto request
    );
}