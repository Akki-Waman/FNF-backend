package com.sipl.ticket.activityLog.controller.impl;

import com.sipl.ticket.activityLog.controller.ActivityLogController;
import com.sipl.ticket.activityLog.service.ActivityLogService;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;
@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityLogControllerImpl implements ActivityLogController {

    private final ActivityLogService activityLogService;

    @Override
    public ResponseEntity<ApiResponseDTO<ActivityLogDashboardDto>> getLatestActivities() {

        log.info("<<START>> getLatestActivities <<START>>");

        ApiResponseDTO<ActivityLogDashboardDto> response =
                activityLogService.getLatestActivities();

        log.info("<<END>> getLatestActivities <<END>>");

        return ResponseEntity.ok(response);
    }





}
