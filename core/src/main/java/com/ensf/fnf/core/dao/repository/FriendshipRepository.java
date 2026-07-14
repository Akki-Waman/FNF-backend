package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.FriendshipEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    @Query("SELECT f FROM FriendshipEntity f JOIN FETCH f.friendUser WHERE f.user.userId = :userId AND (:cursorId IS NULL OR f.friendshipId < :cursorId) ORDER BY f.friendshipId DESC")
    List<FriendshipEntity> findFriendsWithCursor(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);
}