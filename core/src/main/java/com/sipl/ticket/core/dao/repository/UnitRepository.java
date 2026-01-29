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
    Optional<Unit> findActiveById( @Param("unitId") Long unitId);

    @Query("From Unit u where u.isActive = true")
    List<Unit> findAllActive();

    boolean existsByUnitNameIgnoreCase(@Param("unitName") String unitName);

    boolean existsByUnitNameIgnoreCaseAndUnitIdNot(
            @Param("unitName") String unitName,
           @Param("unitId") Long unitId
    );

    @Query(
            "SELECT u FROM Unit u " +
                    "WHERE u.isDelete = false " +
                    "AND ( :isActive IS NULL OR u.isActive = :isActive ) " +
                    "AND ( :search IS NULL " +
                    "   OR LOWER(u.unitName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<Unit> searchUnits(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    @Query(
            "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM Unit u " +
                    "WHERE LOWER(u.unitName) = LOWER(:unitName) " +
                    "AND u.isActive = true"
    )
    boolean existsActiveUnitByName(@Param("unitName") String unitName);


}
