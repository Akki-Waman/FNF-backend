package com.sipl.ticket.activityLog.service;


import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;
import java.util.List;

public interface ActivityLogService {

    void log(String description);

    List<ActivityLogDashboardDto> getLatestActivities();


}
