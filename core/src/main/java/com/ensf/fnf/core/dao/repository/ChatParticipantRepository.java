package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.ChatParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipantEntity, Long> {

    @Query("SELECT cp FROM ChatParticipantEntity cp JOIN FETCH cp.room WHERE cp.user.userId = :userId ORDER BY cp.room.roomId DESC")
    List<ChatParticipantEntity> findActiveRoomsByUserId(@Param("userId") Long userId);

    @Query("SELECT cp FROM ChatParticipantEntity cp WHERE cp.user.userId = :userId AND cp.room.roomId = :roomId")
    Optional<ChatParticipantEntity> verifyUserInRoom(@Param("userId") Long userId, @Param("roomId") Long roomId);
}