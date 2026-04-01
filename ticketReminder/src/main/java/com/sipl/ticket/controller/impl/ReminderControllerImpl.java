package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ReminderController;
import com.sipl.ticket.core.dto.request.ReminderCreateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import com.sipl.ticket.service.ReminderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReminderControllerImpl implements ReminderController {

    private final ReminderService reminderService;

    @Override
    public ResponseEntity<ApiResponseDTO<ReminderResponseDto>> createReminder(
            @RequestBody ReminderCreateRequestDto request) {

        log.info("createReminder called for ticketId={}", request.getTicketId());

        return ResponseEntity.ok(reminderService.createReminder(request));
    }
    @Override
    public ResponseEntity<ApiResponseDTO<ReminderResponseDto>> updateReminder(
            Long reminderId,
            ReminderCreateRequestDto request) {

        log.info("updateReminder called id={}", reminderId);

        return ResponseEntity.ok(
                reminderService.updateReminder(reminderId, request)
        );
    }
}