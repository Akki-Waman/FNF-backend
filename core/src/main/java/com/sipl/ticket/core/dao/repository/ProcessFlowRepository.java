package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProcessFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessFlowRepository extends JpaRepository<ProcessFlow, Long> {

    @Query("SELECT pf FROM ProcessFlow pf " +
            "WHERE pf.screenMaster.id = :screenId")
    Optional<ProcessFlow> findByScreenMasterId(@Param("screenId") Long screenId);


    @Query("SELECT pf FROM ProcessFlow pf " +
            "JOIN MovementFlowMapping mfm ON mfm.processFlow = pf " +
            "WHERE mfm.sequence = :sequence " +
            "AND mfm.movement.movementId = :movementId")
    Optional<ProcessFlow> findBySequenceAndMovementId(@Param("sequence") Integer sequence,
                                                      @Param("movementId") Integer movementId);


    boolean existsByScreenMaster_ScreenMasterId(Long screenId);

    List<ProcessFlow> findByScreenMaster_ScreenMasterId(Long screenId);
}
