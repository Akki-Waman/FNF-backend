package com.sipl.ticket.activityLog.service;


import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;


public interface ActivityLogService {

    void log(String description);

    ApiResponseDTO<ActivityLogDashboardDto> getLatestActivities();


}
