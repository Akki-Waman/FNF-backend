package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query(
            "FROM Shift s WHERE "
                    + "s.isDeleted = false AND "
                    + "s.isActive = true AND "
                    + "(:shiftName IS NULL OR LOWER(s.shiftName) LIKE LOWER(CONCAT('%', :shiftName, '%'))) AND "
                    + "(:startTime IS NULL OR s.startTime >= :startTime) AND "
                    + "(:endTime IS NULL OR s.endTime <= :endTime)"
    )
    Page<Shift> findBySearchQuery(
            @Param("shiftName") String shiftName,
            @Param("startTime") java.time.LocalTime startTime,
            @Param("endTime") java.time.LocalTime endTime,
            Pageable pageable
    );

    boolean existsByShiftNameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String shiftName);

    @Query(
            "SELECT s FROM Shift s " +
                    "WHERE s.isDeleted = false " +
                    "AND s.isActive = true " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR CAST(s.shiftId AS string) LIKE CONCAT('%', :query, '%') " +
                    "      OR LOWER(s.shiftName) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "      OR LOWER(s.description) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "      OR CAST(s.startTime AS string) LIKE CONCAT('%', :query, '%') " +
                    "      OR CAST(s.endTime AS string) LIKE CONCAT('%', :query, '%') " +
                    ")"
    )
    Page<Shift> findByShiftId(
            @Param("query") String query,
            Pageable pageable
    );


}

