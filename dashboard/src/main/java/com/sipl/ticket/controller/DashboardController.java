package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.TaskFilterRequestDTO;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/dashboard")
@CrossOrigin("*")
@Api(tags = "Dashboard APIs")
public interface DashboardController {
    @GetMapping("/ticket-status")
    ResponseEntity<ApiResponseDTO<ChartDataResponseDTO>> getTicketStatus();

    @GetMapping("/ticket-priority")
    ResponseEntity<ApiResponseDTO<ChartDataResponseDTO>> getTicketPriorityStatus();

    @PostMapping("/tasks/calendar")
    ResponseEntity<ApiResponseDTO<TaskResponseDTO>> getTasks(@RequestBody TaskFilterRequestDTO dto);
}
