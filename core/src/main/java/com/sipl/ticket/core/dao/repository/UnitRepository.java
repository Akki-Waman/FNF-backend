package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("From Unit u where u.unitId = :unitId AND u.isActive = true")
    Optional<Unit> findActiveById(Long unitId);

    @Query("From Unit u where u.isActive = true")
    List<Unit> findAllActive();

    boolean existsByUnitNameIgnoreCase(String unitName);

    boolean existsByUnitNameIgnoreCaseAndUnitIdNot(
            String unitName,
            Long unitId
    );

    @Query(
            "SELECT u " +
                    "FROM Unit u " +
                    "WHERE u.isActive = true " +
                    "AND (:unitId IS NULL OR u.unitId = :unitId) " +
                    "ORDER BY u.unitId DESC"
    )
    Page<Unit> searchByUnitId(
            @Param("unitId") Long unitId,
            Pageable pageable
    );

}
