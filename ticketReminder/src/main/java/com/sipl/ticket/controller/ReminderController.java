package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.ReminderCreateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/ticket-reminders")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Reminder APIs")
public interface ReminderController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ReminderResponseDto>> createReminder(
            @Valid @RequestBody ReminderCreateRequestDto request
    );
    @PutMapping("/update/{reminderId}")
    ResponseEntity<ApiResponseDTO<ReminderResponseDto>> updateReminder(
            @PathVariable Long reminderId,
            @Valid @RequestBody ReminderCreateRequestDto request
    );
}