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
    public Map<String, Object> getLatestActivities() {

        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Users loggedInUser = SecurityUtil.getCurrentUser();

            Pageable pageable = PageRequest.of(0, 10);

            List<ActivityLog> logs;

            if (loggedInUser != null) {
                log.info("Fetching logs for logged-in user | userId={}",
                        loggedInUser.getId());

                logs = activityLogRepository
                        .findLatestLogsByUser(loggedInUser, pageable);

            } else {
                log.warn("No authenticated user found, fetching SYSTEM logs");

                logs = activityLogRepository
                        .findLatestLogs(pageable);
            }

            List<Map<String, Object>> aaData = logs.stream()
                    .map(log -> {
                        Map<String, Object> row = new LinkedHashMap<>();
                        row.put("message", log.getDescription());
                        row.put("createdOn", log.getCreatedTime());
                        row.put("module", log.getStaffName());

                        if (log.getCreatedBy() != null) {
                            Users u = log.getCreatedBy();
                            row.put("createdBy",
                                    u.getFirstName() + " " + u.getLastName());
                        } else {
                            row.put("createdBy", "SYSTEM");
                        }

                        return row;
                    })
                    .collect(Collectors.toList());

            response.put("aaData", aaData);

            log.info("Fetched {} activity logs", aaData.size());

        } catch (Exception ex) {

            log.error("Error while fetching activity logs", ex);

            response.put("aaData", List.of());
            response.put("error", true);
            response.put("message", "Unable to fetch activity logs");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return response;
    }
}
