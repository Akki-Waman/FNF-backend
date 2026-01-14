package com.sipl.ticket.activityLog.service.impl;

import com.sipl.ticket.activityLog.service.ActivityLogService;
import com.sipl.ticket.core.dao.entity.ActivityLog;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.ActivityLogRepository;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public List<ActivityLogDashboardDto> getLatestActivities() {

        log.info("ActivityLogService | Request received to fetch latest activities");

        try {
            Users loggedInUser = SecurityUtil.getCurrentUser();
            Pageable pageable = PageRequest.of(0, 10);

            List<ActivityLog> logs;

            if (loggedInUser != null) {
                log.info("Fetching activity logs for userId={}", loggedInUser.getId());

                logs = activityLogRepository
                        .findLatestLogsByUser(loggedInUser, pageable);
            } else {
                log.warn("No authenticated user found, fetching system-wide activity logs");

                logs = activityLogRepository
                        .findLatestLogs(pageable);
            }

            List<ActivityLogDashboardDto> response =
                    logs.stream()
                            .map(this::mapToDashboardDto)
                            .collect(Collectors.toList());

            log.info("Successfully fetched {} activity logs", response.size());

            return response;

        } catch (Exception ex) {

            log.error("Failed to fetch latest activity logs", ex);

            return List.of();
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
