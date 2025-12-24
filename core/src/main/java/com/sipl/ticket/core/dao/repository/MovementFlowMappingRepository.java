package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.MovementFlowMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovementFlowMappingRepository extends JpaRepository<MovementFlowMapping, Long> {

    @Query(value = "SELECT m FROM MovementFlowMapping m " +
            "WHERE m.movement.movementId = :movementId " +
            "ORDER BY m.sequence ASC")
    List<MovementFlowMapping> findByMovementIdOrderBySequenceAsc(@Param("movementId") Integer movementId);

    @Query("SELECT m FROM MovementFlowMapping m " +
            "WHERE m.movement.movementId = :movementId " +
            "AND m.sequence = :sequence")
    Optional<MovementFlowMapping> getCurrentMovementFlowMapping(@Param("movementId") Integer movementId,
                                                                @Param("sequence") Integer sequence);

    @Query("SELECT mfm FROM MovementFlowMapping mfm " +
            "WHERE mfm.processFlow.processFlowId = :processFlowId")
    Optional<MovementFlowMapping> findByProcessFlowId(@Param("processFlowId") Long processFlowId);

    boolean existsByProcessFlow_ProcessFlowId(Long processFlowId);
}
