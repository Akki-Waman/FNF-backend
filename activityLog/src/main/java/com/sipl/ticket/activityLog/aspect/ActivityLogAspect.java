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

        String description = activityLoggable.description();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) continue;

            String value = extractName(arg);
            if (value != null) {
                description = description.replace("{" + i + "}", value);
            }
        }

        activityLogService.log(description);
    }

    private String extractName(Object arg) {
        try {
            for (var method : arg.getClass().getMethods()) {
                if (method.getName().startsWith("get")
                        && method.getName().endsWith("Name")
                        && method.getParameterCount() == 0) {

                    Object value = method.invoke(arg);
                    if (value != null) {
                        return value.toString();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return arg.toString();
    }
}


