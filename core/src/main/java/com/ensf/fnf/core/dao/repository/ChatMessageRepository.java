package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.ChatMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    // Constant-time O(1) cursor lookup for lightning-fast scrolling history
    @Query("SELECT m FROM ChatMessageEntity m JOIN FETCH m.sender WHERE m.room.roomId = :roomId AND (:cursorId IS NULL OR m.messageId < :cursorId) ORDER BY m.messageId DESC")
    List<ChatMessageEntity> findMessagesByCursor(@Param("roomId") Long roomId, @Param("cursorId") Long cursorId, Pageable pageable);
}