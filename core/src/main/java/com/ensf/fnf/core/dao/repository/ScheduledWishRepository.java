package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.ScheduledWishEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledWishRepository extends JpaRepository<ScheduledWishEntity, Long> {

    // Cursor Pagination: O(1) performance lookup avoiding offset delays
    @Query("SELECT w FROM ScheduledWishEntity w WHERE w.sender.userId = :userId AND (:cursorId IS NULL OR w.scheduledWishId < :cursorId) ORDER BY w.scheduledWishId DESC")
    List<ScheduledWishEntity> findByUserIdCursor(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);

    // Optimized polling query for the automated cron engine
    @Query("SELECT w FROM ScheduledWishEntity w JOIN FETCH w.sender JOIN FETCH w.receiver WHERE w.wishStatus = 'PENDING' AND w.scheduledDateTime <= :now")
    List<ScheduledWishEntity> findExecutableWishes(@Param("now") LocalDateTime now, Pageable pageable);
}