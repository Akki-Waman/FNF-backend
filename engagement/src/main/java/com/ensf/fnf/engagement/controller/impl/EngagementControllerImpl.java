package com.ensf.fnf.engagement.controller.impl;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.IntelligenceMetricsDto;
import com.ensf.fnf.engagement.controller.EngagementController;
import com.ensf.fnf.engagement.service.EngagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EngagementControllerImpl implements EngagementController {

    private final EngagementService engagementService;

    @Override
    public ResponseEntity<CommonApiResponse<IntelligenceMetricsDto>> getDashboardMetrics() {
        log.info("<<START>> EngagementControllerImpl :: getDashboardMetrics <<START>>");
        CommonApiResponse<IntelligenceMetricsDto> response = engagementService.computeUserMetrics();
        log.info("<<END>> EngagementControllerImpl :: getDashboardMetrics <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<List<Object>>> getUpcomingCelebrations() {
        log.info("<<START>> EngagementControllerImpl :: getUpcomingCelebrations <<START>>");
        CommonApiResponse<List<Object>> response = engagementService.getUpcomingCelebrations();
        log.info("<<END>> EngagementControllerImpl :: getUpcomingCelebrations <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<List<Object>>> getNotifications() {
        log.info("<<START>> EngagementControllerImpl :: getNotifications <<START>>");
        CommonApiResponse<List<Object>> response = engagementService.getNotifications();
        log.info("<<END>> EngagementControllerImpl :: getNotifications <<END>>");
        return ResponseEntity.ok(response);
    }
}