package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(
            "SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM Department d " +
                    "WHERE LOWER(d.departmentName) = LOWER(:departmentName) " +
                    "AND d.branch.branchId = :branchId " +
                    "AND d.isDelete = false"
    )
    boolean existsActiveDepartmentForBranch(
            @Param("departmentName") String departmentName,
            @Param("branchId") Integer branchId
    );

    boolean existsByDepartmentNameIgnoreCaseAndDepartmentIdNot(
            String departmentName, Long departmentId
    );

    @Query(
            "SELECT d FROM Department d " +
                    "WHERE d.isDelete = false " +
                    "AND ( :branchId IS NULL OR d.branch.branchId = :branchId ) " +
                    "AND ( :isActive IS NULL OR d.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "   OR LOWER(d.departmentName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Department> searchDepartments(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );




}
