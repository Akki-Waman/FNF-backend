package com.sipl.ticket.activityLog.service.impl;

import com.sipl.ticket.activityLog.service.ActivityLogService;
import com.sipl.ticket.core.dao.entity.ActivityLog;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.ActivityLogRepository;
import com.sipl.ticket.core.util.RequestContextUtil;
import com.sipl.ticket.core.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}
