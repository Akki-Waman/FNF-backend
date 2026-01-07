package com.sipl.ticket.activityLog.aspect;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.activityLog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogService activityLogService;

    @AfterReturning("@annotation(activityLoggable)")
    public void logActivity(
            JoinPoint joinPoint,
            ActivityLoggable activityLoggable) {

        String description =
                activityLoggable.action() + " " +
                        activityLoggable.module();

        activityLogService.log(description);
    }
}
