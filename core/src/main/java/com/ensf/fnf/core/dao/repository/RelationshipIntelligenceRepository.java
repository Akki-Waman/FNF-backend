package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.RelationshipIntelligenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RelationshipIntelligenceRepository extends JpaRepository<RelationshipIntelligenceEntity, Long> {

    @Query("SELECT r FROM RelationshipIntelligenceEntity r WHERE r.user.userId = :userId")
    Optional<RelationshipIntelligenceEntity> findByUserId(@Param("userId") Long userId);
}