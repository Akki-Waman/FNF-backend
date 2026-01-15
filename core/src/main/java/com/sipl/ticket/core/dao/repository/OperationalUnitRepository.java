package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.MovementFlowMapping;
import com.sipl.ticket.core.dao.entity.OperationalUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationalUnitRepository extends JpaRepository<OperationalUnit, Long> {
    List<OperationalUnit> findByIsActiveTrue();

    @Query("SELECT o FROM OperationalUnit o WHERE o.division.divisionId = :divisionId AND o.isActive = true")
    List<OperationalUnit> findByDivisionId(@Param("divisionId") Long divisionId);
}
