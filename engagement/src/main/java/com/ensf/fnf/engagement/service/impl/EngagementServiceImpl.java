package com.ensf.fnf.engagement.service.impl;

import com.ensf.fnf.core.dao.entity.RelationshipIntelligenceEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.RelationshipIntelligenceRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.IntelligenceMetricsDto;
import com.ensf.fnf.core.mapper.IntelligenceMapper;
import com.ensf.fnf.engagement.service.EngagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngagementServiceImpl implements EngagementService {

    private final UserRepository userRepository;
    private final RelationshipIntelligenceRepository intelligenceRepository;
    private final IntelligenceMapper intelligenceMapper;

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<IntelligenceMetricsDto> computeUserMetrics() {
        UserEntity user = getAuthenticatedUser();

        RelationshipIntelligenceEntity analytics = loadOrCreateMetrics(user);

        log.debug("Successfully loaded intelligence metrics for user {}", user.getUserId());
        return CommonApiResponse.<IntelligenceMetricsDto>builder()
                .success(true)
                .data(intelligenceMapper.toDto(analytics))
                .build();
    }

    // ==========================================
    // PRIVATE HELPER METHODS
    // ==========================================

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    private RelationshipIntelligenceEntity loadOrCreateMetrics(UserEntity user) {
        return intelligenceRepository.findByUserId(user.getUserId())
                .orElseGet(() -> generateDefaultBaseline(user));
    }

    private RelationshipIntelligenceEntity generateDefaultBaseline(UserEntity user) {
        log.info("Generating default intelligence baseline for new user: {}", user.getUserId());
        return RelationshipIntelligenceEntity.builder()
                .user(user)
                .relationshipScore(50) // Neutral starting score
                .peopleNotWished(0)
                .upcomingCelebrationsCount(0)
                .friendActivityTier("MEDIUM")
                .lastCalculatedAt(LocalDateTime.now())
                .build();
    }
    @Override
    public CommonApiResponse<java.util.List<Object>> getUpcomingCelebrations() {
        UserEntity user = getAuthenticatedUser();
        log.info("Compiling celebration feed for user: {}", user.getUserId());

        java.util.List<Object> feed = aggregateCelebrationFeed(user.getUserId());
        return CommonApiResponse.<java.util.List<Object>>builder().success(true).data(feed).build();
    }

    @Override
    public CommonApiResponse<java.util.List<Object>> getNotifications() {
        UserEntity user = getAuthenticatedUser();
        log.info("Fetching notifications for user: {}", user.getUserId());

        java.util.List<Object> notifications = fetchNotificationLogs(user.getUserId());
        return CommonApiResponse.<java.util.List<Object>>builder().success(true).data(notifications).build();
    }

    // --- Private Helpers ---
    private java.util.List<Object> aggregateCelebrationFeed(Long userId) {
        log.debug("Scanning FamilyMember dates for upcoming events");
        // In production: Query `FamilyMemberRepository` where MONTH(dateOfBirth) == currentMonth
        return java.util.Collections.emptyList();
    }

    private java.util.List<Object> fetchNotificationLogs(Long userId) {
        log.debug("Pulling recent activity logs from cache/DB");
        return java.util.Collections.emptyList();
    }
}