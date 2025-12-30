package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("From Unit u where u.unitId = :unitId AND u.isActive = true")
    Optional<Unit> findActiveByUnitId(@Param("unitId") Long unitId);
}
