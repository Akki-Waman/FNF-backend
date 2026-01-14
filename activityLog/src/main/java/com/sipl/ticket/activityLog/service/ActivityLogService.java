package com.sipl.ticket.activityLog.service;


import java.util.Map;

public interface ActivityLogService {

    void log(String description);

    Map<String, Object> getLatestActivities();

}
