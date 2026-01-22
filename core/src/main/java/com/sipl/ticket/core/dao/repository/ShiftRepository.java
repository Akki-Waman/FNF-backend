package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable
    );

    boolean existsByShiftNameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(String shiftName);

    @Query(
            "SELECT s FROM Shift s " +
                    "WHERE (s.isDeleted = false OR s.isDeleted IS NULL) " +
                    "AND (:isActive IS NULL OR s.isActive = :isActive) " +
                    "AND (:branchId IS NULL OR s.branch.branchId = :branchId) " +
                    "AND (:query IS NULL OR :query = '' " +
                    "     OR LOWER(s.shiftName) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "     OR LOWER(s.description) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "     OR CAST(s.startTime AS string) LIKE CONCAT('%', :query, '%') " +
                    "     OR CAST(s.endTime AS string) LIKE CONCAT('%', :query, '%') " +
                    ")"
    )
    Page<Shift> searchShifts(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );


    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
                    "FROM Shift s " +
                    "WHERE LOWER(s.shiftName) = LOWER(:shiftName) " +
                    "AND s.shiftId <> :shiftId " +
                    "AND s.isActive = true " +
                    "AND (s.isDeleted = false OR s.isDeleted IS NULL)"
    )
    boolean existsByShiftNameAndIdNot(
            @Param("shiftName") String shiftName,
            @Param("shiftId") Long shiftId
    );

    @Query("From Shift s where s.branch.branchId = :branchId")
    List<Shift> findByBranchId(@Param("branchId") Integer branchId);

    boolean existsByShiftNameIgnoreCaseAndBranch_BranchIdAndIsDeletedFalse(
            String shiftName, Integer branchId
    );
    boolean existsByShiftNameIgnoreCaseAndBranch_BranchIdAndShiftIdNotAndIsDeletedFalse(
            String shiftName, Integer branchId, Long shiftId
    );


}

