package com.ensf.fnf.engagement.service;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.IntelligenceMetricsDto;

import java.util.List;

public interface EngagementService {
    CommonApiResponse<IntelligenceMetricsDto> computeUserMetrics();
    CommonApiResponse<List<Object>> getUpcomingCelebrations();
    CommonApiResponse<List<Object>> getNotifications();
}