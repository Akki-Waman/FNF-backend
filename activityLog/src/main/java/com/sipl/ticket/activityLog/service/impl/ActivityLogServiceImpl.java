package com.sipl.ticket.activityLog.service.impl;

import com.sipl.ticket.activityLog.service.ActivityLogService;
import com.sipl.ticket.core.dao.entity.ActivityLog;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.ActivityLogRepository;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.util.RequestContextUtil;
import com.sipl.ticket.core.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Async
    public void log(String description) {

        Users currentUser = SecurityUtil.getCurrentUser();
        ActivityLog log = new ActivityLog();

        log.setDescription(description);
        log.setPerformedBy(currentUser);
        log.setStaffName(SecurityUtil.getStaffName());
        log.setIpAddress(RequestContextUtil.getClientIp());

        activityLogRepository.save(log);
    }

    @Override
    public ApiResponseDTO<ActivityLogDashboardDto> getLatestActivities() {

        try {
            Users loggedInUser = SecurityUtil.getCurrentUser();
            Pageable pageable = PageRequest.of(0, 10);

            log.debug("ActivityLogService | Pageable initialized | page=0, size=10");

            List<ActivityLog> logs;

            if (loggedInUser != null) {

                log.info(
                        "ActivityLogService | Fetching logs for logged-in user | userId={}",
                        loggedInUser.getId()
                );

                logs = activityLogRepository
                        .findLatestLogsByUser(loggedInUser, pageable);

            } else {

                log.warn(
                        "ActivityLogService | No authenticated user found | fetching system-wide logs"
                );

                logs = activityLogRepository
                        .findLatestLogs(pageable);
            }

            log.info(
                    "ActivityLogService | Logs fetched successfully | count={}",
                    logs.size()
            );

            List<ActivityLogDashboardDto> dtoList = logs.stream()
                    .map(this::mapToDashboardDto)
                    .collect(Collectors.toList());

            ApiResponseDTO<ActivityLogDashboardDto> response = new ApiResponseDTO<>();

            response.setDataList(dtoList);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Latest activity logs fetched successfully");
            response.setError(false);

            log.info(
                    "ActivityLogService | getLatestActivities | END | returnedCount={}",
                    dtoList.size()
            );

            return response;

        } catch (Exception ex) {

            log.error(
                    "ActivityLogService | getLatestActivities | ERROR",
                    ex
            );

            ApiResponseDTO<ActivityLogDashboardDto> response = new ApiResponseDTO<>();

            response.setDataList(Collections.emptyList());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Failed to fetch latest activity logs");
            response.setError(true);

            return response;
        }
    }



    private ActivityLogDashboardDto mapToDashboardDto(ActivityLog log) {

        ActivityLogDashboardDto dto = new ActivityLogDashboardDto();

        dto.setMessage(log.getDescription());
        dto.setModule(log.getStaffName());
        dto.setCreatedTime(log.getCreatedTime());

        if (log.getCreatedBy() != null) {
            Users u = log.getCreatedBy();
            dto.setCreatedBy(u.getFirstName() + " " + u.getLastName());
        } else {
            dto.setCreatedBy("SYSTEM");
        }

        return dto;
    }


}
