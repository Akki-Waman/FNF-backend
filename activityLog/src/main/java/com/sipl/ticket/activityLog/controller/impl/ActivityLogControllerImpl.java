package com.sipl.ticket.activityLog.controller.impl;

import com.sipl.ticket.activityLog.controller.ActivityLogController;
import com.sipl.ticket.activityLog.service.ActivityLogService;
import com.sipl.ticket.core.dto.response.ActivityLogDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityLogControllerImpl implements ActivityLogController {

    private final ActivityLogService activityLogService;

    @Override
    public ResponseEntity<Map<String, Object>> getLatestActivities() {

        log.info("<<Start>> getLatestActivities controller");

        Map<String, Object> response =
                activityLogService.getLatestActivities();

        log.info("<<End>> getLatestActivities controller");

        return ResponseEntity.ok(response);
    }

}
