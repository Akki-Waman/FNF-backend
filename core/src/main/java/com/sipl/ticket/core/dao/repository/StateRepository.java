package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    boolean existsByStateNameIgnoreCase(String stateName);

    boolean existsByStateNameIgnoreCaseAndStateIdNot(
            String stateName,
            Long stateId
    );

    List<State> findByIsActiveTrue();

    @Query(
            "SELECT s " +
                    "FROM State s " +
                    "WHERE s.isActive = true " +
                    "AND (:stateId IS NULL OR s.stateId = :stateId) " +
                    "ORDER BY s.stateId DESC"
    )
    Page<State> searchStates(
            @Param("stateId") Long stateId,
            Pageable pageable
    );
}
