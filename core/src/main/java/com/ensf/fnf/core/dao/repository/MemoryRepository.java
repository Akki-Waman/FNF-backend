package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.MemoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MemoryRepository extends JpaRepository<MemoryEntity, Long> {

    @Query("SELECT m FROM MemoryEntity m WHERE m.user.userId = :userId AND (:mediaType IS NULL OR m.mediaType = :mediaType) AND (:cursorId IS NULL OR m.memoryId < :cursorId) ORDER BY m.memoryId DESC")
    List<MemoryEntity> findUserGalleryWithCursor(@Param("userId") Long userId, @Param("mediaType") String mediaType, @Param("cursorId") Long cursorId, Pageable pageable);
}