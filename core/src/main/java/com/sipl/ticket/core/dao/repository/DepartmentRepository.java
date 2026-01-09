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

    boolean existsByDepartmentNameIgnoreCase(String departmentName);

    boolean existsByDepartmentNameIgnoreCaseAndDepartmentIdNot(
            String departmentName, Long departmentId
    );

    @Query(
            "SELECT d FROM Department d " +
                    "WHERE d.isActive = true " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR CAST(d.departmentId AS string) LIKE CONCAT('%', :query, '%') " +
                    "      OR LOWER(d.departmentName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Department> searchDepartments(
            @Param("query") String query,
            Pageable pageable
    );


}
