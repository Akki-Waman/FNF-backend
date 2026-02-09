package com.sipl.ticket.activityLog.service;


import com.sipl.ticket.activityLog.dto.response.ActivityLogDashboardDto;
import com.sipl.ticket.core.dto.request.ActivityLogReportRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;

import javax.servlet.http.HttpServletResponse;


public interface ActivityLogService {

    void log(String description);

    ApiResponseDTO<ActivityLogDashboardDto> getLatestActivities();


    void exportActivityLogs(ActivityLogReportRequestDto requestDto, String format, HttpServletResponse response);
}
