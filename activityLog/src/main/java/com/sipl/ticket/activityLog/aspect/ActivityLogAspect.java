package com.sipl.ticket.activityLog.aspect;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.activityLog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
                        && method.getParameterCount() == 0
                        && List.class.isAssignableFrom(method.getReturnType())) {

                    Object value = method.invoke(arg);

                    if (value != null && value instanceof List) {
                        List<?> list = (List<?>) value;

                        if (!list.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < list.size(); i++) {
                                if (i > 0) sb.append(",");
                                sb.append(String.valueOf(list.get(i)));
                            }
                            return sb.toString();
                        }
                    }
                }
            }

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


