package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.FriendRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    @Query("SELECT r FROM FriendRequestEntity r JOIN FETCH r.sender WHERE r.receiver.userId = :receiverId AND r.requestStatus = 'PENDING' AND (:cursorId IS NULL OR r.friendRequestId < :cursorId) ORDER BY r.friendRequestId DESC")
    List<FriendRequestEntity> findPendingIncomingRequests(@Param("receiverId") Long receiverId, @Param("cursorId") Long cursorId, Pageable pageable);
}