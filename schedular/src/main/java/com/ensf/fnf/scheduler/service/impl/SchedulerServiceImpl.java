package com.ensf.fnf.scheduler.service.impl;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dao.entity.ScheduledWishEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.FamilyMemberRepository;
import com.ensf.fnf.core.dao.repository.ScheduledWishRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.ScheduleWishRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.ScheduledWishResponseDto;
import com.ensf.fnf.core.mapper.ScheduledWishMapper;
import com.ensf.fnf.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final UserRepository userRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final ScheduledWishRepository wishRepository;
    private final ScheduledWishMapper wishMapper;

    @Override
    @Transactional
    public CommonApiResponse<String> createScheduledWish(ScheduleWishRequestDto dto) {
        UserEntity sender = getAuthenticatedUser();
        validateFutureDate(dto.getScheduledDateTime());
        FamilyMemberEntity receiver = resolveReceiverNode(dto.getReceiverMemberId());

        persistWishToDatabase(sender, receiver, dto);

        return CommonApiResponse.<String>builder().success(true).message("Wish successfully queued for execution.").build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<CursorPageResponse<ScheduledWishResponseDto>> fetchPendingWishes(Long cursorId, int limit) {
        UserEntity user = getAuthenticatedUser();

        List<ScheduledWishEntity> entities = wishRepository.findByUserIdCursor(
                user.getUserId(), cursorId, PageRequest.of(0, limit + 1));

        CursorPageResponse<ScheduledWishResponseDto> response = formatCursorResponse(entities, limit);
        return CommonApiResponse.<CursorPageResponse<ScheduledWishResponseDto>>builder().success(true).data(response).build();
    }

    @Override
    @Transactional
    public CommonApiResponse<String> deleteScheduledWish(Long wishId) {
        UserEntity user = getAuthenticatedUser();
        verifyOwnershipAndCancel(user.getUserId(), wishId);

        return CommonApiResponse.<String>builder().success(true).message("Scheduled wish cancelled.").build();
    }

    // ==========================================
    // PRIVATE HELPER METHODS
    // ==========================================

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    private void validateFutureDate(LocalDateTime scheduledDate) {
        if (scheduledDate.isBefore(LocalDateTime.now())) {
            log.error("Attempted to schedule a wish in the past: {}", scheduledDate);
            throw new IllegalArgumentException("Execution time must be in the future");
        }
    }

    private FamilyMemberEntity resolveReceiverNode(Long receiverId) {
        return familyMemberRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Target family node does not exist"));
    }

    private void persistWishToDatabase(UserEntity sender, FamilyMemberEntity receiver, ScheduleWishRequestDto dto) {
        log.debug("Persisting scheduled wish for sender: {}", sender.getUserId());
        ScheduledWishEntity wish = ScheduledWishEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .wishMessage(dto.getWishMessage())
                .scheduledDateTime(dto.getScheduledDateTime())
                .mediaTemplateUrl(dto.getMediaTemplateUrl())
                .wishStatus("PENDING")
                .build();
        wishRepository.save(wish);
    }

    private void verifyOwnershipAndCancel(Long userId, Long wishId) {
        ScheduledWishEntity wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new RuntimeException("Wish not found"));

        if (!wish.getSender().getUserId().equals(userId)) {
            log.warn("Security violation: User {} attempted to cancel wish {} they do not own", userId, wishId);
            throw new SecurityException("Unauthorized action");
        }

        wishRepository.delete(wish);
        log.debug("Wish {} successfully deleted", wishId);
    }

    private CursorPageResponse<ScheduledWishResponseDto> formatCursorResponse(List<ScheduledWishEntity> entities, int limit) {
        boolean hasNext = entities.size() > limit;
        if (hasNext) {
            entities.remove(limit);
        }
        Long nextCursor = entities.isEmpty() ? null : entities.get(entities.size() - 1).getScheduledWishId();

        return CursorPageResponse.<ScheduledWishResponseDto>builder()
                .content(wishMapper.toDtoList(entities))
                .hasNext(hasNext)
                .nextCursorId(nextCursor)
                .build();
    }

    @Override
    public CommonApiResponse<java.util.List<Object>> getTemplates() {
        log.info("Fetching global wish templates");
        java.util.List<Object> templates = retrieveStaticTemplates();
        return CommonApiResponse.<java.util.List<Object>>builder().success(true).data(templates).build();
    }

    private java.util.List<Object> retrieveStaticTemplates() {
        // Return Cloudfront/S3 URLs of pre-made cards
        return java.util.List.of(
                java.util.Map.of("id", 1, "text", "Happy Birthday! Wishing you joy.", "backgroundUrl", "https://cdn.fnf.com/templates/bday1.png"),
                java.util.Map.of("id", 2, "text", "Happy Anniversary!", "backgroundUrl", "https://cdn.fnf.com/templates/anniv1.png")
        );
    }
}